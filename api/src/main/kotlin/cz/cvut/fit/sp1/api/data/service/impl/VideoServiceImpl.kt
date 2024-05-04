package cz.cvut.fit.sp1.api.data.service.impl

import cz.cvut.fit.sp1.api.component.FileStorage
import cz.cvut.fit.sp1.api.component.MediaProcessor
import cz.cvut.fit.sp1.api.component.mapper.VideoMapper
import cz.cvut.fit.sp1.api.configuration.StoragePathProperties
import cz.cvut.fit.sp1.api.data.dto.VideoDto
import cz.cvut.fit.sp1.api.data.dto.search.SearchMediaParamsDto
import cz.cvut.fit.sp1.api.data.dto.search.SearchVideoDto
import cz.cvut.fit.sp1.api.data.model.media.Video
import cz.cvut.fit.sp1.api.data.repository.VideoRepository
import cz.cvut.fit.sp1.api.data.service.interfaces.TagService
import cz.cvut.fit.sp1.api.data.service.interfaces.UserAccountService
import cz.cvut.fit.sp1.api.data.service.interfaces.VideoService
import cz.cvut.fit.sp1.api.exception.EntityNotFoundException
import cz.cvut.fit.sp1.api.exception.exceptioncodes.VideoExceptionCodes
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class VideoServiceImpl(
    private val videoRepository: VideoRepository,
    private val fileStorage: FileStorage,
    private val videoMapper: VideoMapper,
    private val restTemplate: RestTemplate,
    private val tagService: TagService,
    private val storagePathProperties: StoragePathProperties,
    private val userAccountService: UserAccountService,
) : VideoService {
    override fun findById(id: Long): Optional<Video> {
        return videoRepository.findById(id)
    }

    override fun getByIdOrThrow(id: Long): Video {
        return findById(id)
            .orElseThrow { throw EntityNotFoundException(VideoExceptionCodes.VIDEO_NOT_FOUND, id) }
    }

    override fun findAll(paramsDto: SearchMediaParamsDto<Video>?): SearchVideoDto? {
        if (paramsDto == null) {
            return null
        }
        val specification = paramsDto.buildSpecification()
        val pageable = paramsDto.buildPageable()
        val page = videoRepository.findAll(specification, pageable)
        val videos =
            page.content.stream()
                .map { videoMapper.toDto(it)!! }
                .toList()
        return SearchVideoDto(
            videos = videos,
            currentPage = page.number + 1,
            totalPages = page.totalPages,
            totalMatches = page.totalElements,
        )
    }

    @Transactional
    override fun create(
        video: MultipartFile,
        videoDto: VideoDto,
    ): Video {
        val videoEntity = fetchVideoInfo(video)
        enrichWithModels(videoDto, videoEntity)
        videoEntity.name = videoDto.name!!
        videoEntity.description = videoDto.description
        return videoRepository.save(videoEntity)
    }

    fun fetchVideoInfo(video: MultipartFile): Video {
        val processor = MediaProcessor(video, fileStorage, restTemplate, storagePathProperties = storagePathProperties)
        return processor.extractVideoInfo()
    }

    private fun enrichWithModels(videoDto: VideoDto, video: Video) {
        val user = userAccountService.getByAuthentication()
        val tags = tagService.getAllByIds(videoDto.tagIds!!)
        video.createdBy = user
        user.createdMedia.add(video)
        video.tags = tags
        tags.forEach { it.media.add(video) }
    }

    @Transactional
    override fun toggleLike(videoId: Long, userId: Long): Video {
        val video = getByIdOrThrow(videoId)
        val user = userAccountService.getByIdOrThrow(userId)
        if (user.likedMedia.contains(video)) {
            user.likedMedia.remove(video)
            video.likedBy.remove(user)
        } else {
            user.likedMedia.add(video)
            video.likedBy.add(user)
        }
        return videoRepository.save(video)
    }

    override fun delete(id: Long) {
        val video = getByIdOrThrow(id)
        fileStorage.delete(video.path)
        video.tags.forEach { it.media.remove(video) }
        video.likedBy.forEach { it.likedMedia.remove(video) }
        videoRepository.delete(video)
    }

    override fun update(id: Long, videoDto: VideoDto): Video {
        val video = getByIdOrThrow(id)
        video.name = videoDto.name!!
        video.description = videoDto.description
        enrichWithModels(videoDto, video)
        return videoRepository.save(video)
    }

    override fun countAll(): Long {
        return videoRepository.count()
    }
}

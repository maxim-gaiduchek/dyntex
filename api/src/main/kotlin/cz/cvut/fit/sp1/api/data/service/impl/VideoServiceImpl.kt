package cz.cvut.fit.sp1.api.data.service.impl

import cz.cvut.fit.sp1.api.component.FileStorage
import cz.cvut.fit.sp1.api.component.MediaProcessor
import cz.cvut.fit.sp1.api.component.mapper.VideoMapper
import cz.cvut.fit.sp1.api.data.dto.search.SearchMediaParamsDto
import cz.cvut.fit.sp1.api.data.dto.search.SearchVideoDto
import cz.cvut.fit.sp1.api.data.model.media.Video
import cz.cvut.fit.sp1.api.data.repository.VideoRepository
import cz.cvut.fit.sp1.api.data.service.interfaces.VideoService
import cz.cvut.fit.sp1.api.exception.EntityNotFoundException
import cz.cvut.fit.sp1.api.exception.exceptioncodes.VideoExceptionCodes
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class VideoServiceImpl(
    private val videoRepository: VideoRepository,
    private val fileStorage: FileStorage,
    private val videoMapper: VideoMapper,
    private val restTemplate: RestTemplate,
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

    override fun create(video: MultipartFile): Video {
        val processor = MediaProcessor(video, fileStorage, restTemplate)
        val videoEntity = processor.extractVideoInfo()
        videoRepository.save(videoEntity)

        return videoEntity
    }
}

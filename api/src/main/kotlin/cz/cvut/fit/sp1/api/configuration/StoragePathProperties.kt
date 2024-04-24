package cz.cvut.fit.sp1.api.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import kotlin.io.path.Path

@ConfigurationProperties(prefix = "storage")
class StoragePathProperties {
    var mediaPath: String = Path(System.getProperty("user.home"), "sp1", "storage").toString()
}

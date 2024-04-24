package cz.cvut.fit.sp1.api.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "storage")
class StoragePathProperties {
    var mediaPath: String = "/Users/maksymgaiduchek/Library/CloudStorage/OneDrive-Личная/Universities/CTU/SP1/storage"
}

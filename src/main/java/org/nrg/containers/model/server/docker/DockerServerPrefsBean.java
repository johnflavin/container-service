package org.nrg.containers.model.server.docker;

import com.google.common.base.MoreObjects;
import org.nrg.framework.configuration.ConfigPaths;
import org.nrg.prefs.annotations.NrgPreference;
import org.nrg.prefs.annotations.NrgPreferenceBean;
import org.nrg.prefs.beans.AbstractPreferenceBean;
import org.nrg.prefs.exceptions.InvalidPreferenceName;
import org.nrg.prefs.services.NrgPreferenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Objects;

/**
 * Docker server settings, stored as a prefs bean.
 * Deprecated in favor of storing docker server(s) in a hibernate table.
 * Use DockerServer / DockerServerService for the pojo, and
 *  DockerServerEntity / DockerServerEntityService for the entity.
 * @since 1.3
 */
@NrgPreferenceBean(toolId = "docker-server",
    toolName = "Docker Server Prefs",
    description = "All the preferences that define a Docker Server")
@Deprecated
public class DockerServerPrefsBean extends AbstractPreferenceBean {
    private static final Logger _log = LoggerFactory.getLogger(DockerServerPrefsBean.class);

    @Autowired
    public DockerServerPrefsBean(final NrgPreferenceService preferenceService) {
        super(preferenceService);
    }

    public DockerServerPrefsBean(final NrgPreferenceService preferenceService, final ConfigPaths configFolderPaths) {
        super(preferenceService, configFolderPaths);
    }

    public void fromPojo(final DockerServerBase.DockerServer dockerServer) throws InvalidPreferenceName {
        setHost(dockerServer.host());
        setName(dockerServer.name());
        setCertPath(dockerServer.certPath());
        setLastEventCheckTime(new Date()); // Initialize with current time
    }

    @NrgPreference(defaultValue = "Local socket")
    public String getName() {
        return getValue("name");
    }

    public void setName(final String name) {
        if (name != null) {
            try {
                set(name, "name");
            } catch (InvalidPreferenceName e) {
                _log.error("Error setting Docker server preference \"name\".", e.getMessage());
            }
        }
    }

    @NrgPreference(defaultValue = "unix:///var/run/docker.sock")
    public String getHost() {
        return getValue("host");
    }

    public void setHost(final String host) {
        if (host != null) {
            try {
                set(host, "host");
            } catch (InvalidPreferenceName e) {
                _log.error("Error setting Docker server preference \"host\".", e.getMessage());
            }
        }
    }

    @NrgPreference
    public String getCertPath() {
        return getValue("certPath");
    }

    public void setCertPath(final String certPath) {
        if (certPath != null) {
            try {
                set(certPath, "certPath");
            } catch (InvalidPreferenceName e) {
                _log.error("Error setting Docker server preference \"certPath\".", e.getMessage());
            }
        }
    }

    @NrgPreference
    public Date getLastEventCheckTime() {
        return getDateValue("lastEventCheckTime");
    }

    public void setLastEventCheckTime(final Date lastEventCheckTime) {
        if (lastEventCheckTime != null) {
            try {
                setDateValue(lastEventCheckTime, "lastEventCheckTime");
            } catch (InvalidPreferenceName e) {
                _log.error("Error setting Docker server value \"lastEventCheckTime\".", e.getMessage());
            }
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", getName())
                .add("host", getHost())
                .add("certPath", getCertPath())
                .add("lastEventCheckTime", getLastEventCheckTime())
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DockerServerPrefsBean that = (DockerServerPrefsBean) o;

        return Objects.equals(this.getName(), that.getName()) &&
                Objects.equals(this.getHost(), that.getHost()) &&
                Objects.equals(this.getCertPath(), that.getCertPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getHost(), getCertPath());
    }
}

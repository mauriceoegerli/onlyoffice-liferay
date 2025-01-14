/**
 *
 * (c) Copyright Ascensio System SIA 2022
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package onlyoffice.integration;

import java.security.MessageDigest;
import java.util.Base64;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import onlyoffice.integration.config.OnlyOfficeConfigManager;

@Component(
    service = OnlyOfficeHasher.class
)
public class OnlyOfficeHasher {
    public String getHash(Long id) {
        try
        {
            String str = Long.toString(id);

            String payload = getHashString(str + getSecret()) + "?" + str;
            return Base64.getUrlEncoder().encodeToString(payload.getBytes("UTF-8"));
        }
        catch (Exception ex)
        {
            _log.error(ex.getMessage(), ex);
        }
        return "";
    }

    public Long validate(String base64)
    {
        try
        {
            String payload = new String(Base64.getUrlDecoder().decode(base64), "UTF-8");

            String[] payloadParts = payload.split("\\?");

            String hash = getHashString(payloadParts[1] + getSecret());
            if (hash.equals(payloadParts[0]))
            {
                return Long.parseLong(payloadParts[1]);
            }
        } catch (Exception ex)
        {
            _log.error(ex.getMessage(), ex);
        }
        return (long) 0;
    }

    private String getSecret() {
        return _config.getSecret();
    }

    private String getHashString(String str)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(str.getBytes());
            String b64 = Base64.getEncoder().encodeToString(digest);

            return b64;
        } catch (Exception ex)
        {
            _log.error(ex.getMessage(), ex);
        }
        return "";
    }

    @Reference
    private OnlyOfficeConfigManager _config;

    private static final Log _log = LogFactoryUtil.getLog(OnlyOfficeHasher.class);
}

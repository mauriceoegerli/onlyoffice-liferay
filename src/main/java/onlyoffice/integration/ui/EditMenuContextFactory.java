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

package onlyoffice.integration.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.document.library.display.context.DLDisplayContextFactory;
import com.liferay.document.library.display.context.DLEditFileEntryDisplayContext;
import com.liferay.document.library.display.context.DLViewFileVersionDisplayContext;
import com.liferay.document.library.kernel.model.DLFileEntryType;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.FileShortcut;
import com.liferay.portal.kernel.repository.model.FileVersion;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactory;

import onlyoffice.integration.OnlyOfficeConvertUtils;
import onlyoffice.integration.OnlyOfficeUtils;

@Component(immediate = true, service = DLDisplayContextFactory.class)
public class EditMenuContextFactory
    implements DLDisplayContextFactory {

    public DLEditFileEntryDisplayContext getDLEditFileEntryDisplayContext(
        DLEditFileEntryDisplayContext parentDLEditFileEntryDisplayContext,
        HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse,
        DLFileEntryType dlFileEntryType) {

        return parentDLEditFileEntryDisplayContext;
    }

    public DLEditFileEntryDisplayContext getDLEditFileEntryDisplayContext(
        DLEditFileEntryDisplayContext parentDLEditFileEntryDisplayContext,
        HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse, FileEntry fileEntry) {

        return parentDLEditFileEntryDisplayContext;
    }

    public DLViewFileVersionDisplayContext getDLViewFileVersionDisplayContext(
        DLViewFileVersionDisplayContext parentDLViewFileVersionDisplayContext,
        HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse, FileShortcut fileShortcut) {

        return parentDLViewFileVersionDisplayContext;
    }

    public DLViewFileVersionDisplayContext getDLViewFileVersionDisplayContext(
        DLViewFileVersionDisplayContext parentDLViewFileVersionDisplayContext,
        HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse, FileVersion fileVersion) {

        return new EditMenuContext(
            parentDLViewFileVersionDisplayContext.getUuid(),
            parentDLViewFileVersionDisplayContext, httpServletRequest,
            httpServletResponse, fileVersion, _utils, _convertUtils, _permissionFactory);
    }

    @Reference
    private OnlyOfficeConvertUtils _convertUtils;

    @Reference
    private OnlyOfficeUtils _utils;

    @Reference
    private PermissionCheckerFactory _permissionFactory;
}
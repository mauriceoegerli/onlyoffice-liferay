<%--
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
 --%>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>


<%@ page import="com.liferay.document.library.kernel.service.DLAppLocalServiceUtil" %>
<%@ page import="com.liferay.portal.kernel.repository.model.FileEntry" %>

<%@ page import="org.osgi.framework.BundleContext" %>
<%@ page import="org.osgi.framework.FrameworkUtil" %>

<%@ page import="onlyoffice.integration.OnlyOfficeUtils" %>

<liferay-theme:defineObjects />

<portlet:defineObjects />


<%
    BundleContext bc = FrameworkUtil.getBundle(OnlyOfficeUtils.class).getBundleContext();

    Long fileEntryId = (Long)request.getAttribute("fileEntryId");
    String version = (String)request.getAttribute("version");
    FileEntry fileEntry = DLAppLocalServiceUtil.getFileEntry(fileEntryId);
    OnlyOfficeUtils utils = bc.getService(bc.getServiceReference(OnlyOfficeUtils.class));
%>

<div id="onlyoffice-preview">
    <div class="preview-file-error-container" style="display: none">
        <h3><liferay-ui:message key="onlyoffice-preview-error-no-preview-available" /></h3>
        <p class="text-secondary">
            <liferay-ui:message key="onlyoffice-editor-error-docs-api-undefined" />
        </p>
    </div>
    <div class="preview-file" style="height: 75vh">
        <div id="scriptApi"></div>
        <div id="placeholder"></div>
    </div>
    <aui:script>
        (function () {
            var divScriptApi = document.getElementById("scriptApi");
            var scriptApi = document.createElement("script");

            scriptApi.setAttribute("type", "text/javascript");
            scriptApi.setAttribute("src", "<%= utils.getDocServerUrl() %>web-apps/apps/api/documents/api.js");

            scriptApi.onload = scriptApi.onerror = function() {
                if (typeof DocsAPI === "undefined") {
                    var divOnlyofficePreview = document.getElementById("onlyoffice-preview");
                    divOnlyofficePreview.querySelector("div.preview-file-error-container").style.display = "block";
                    divOnlyofficePreview.querySelector("div.preview-file").style.display = "none";
                } else {
                    var config = JSON.parse('<%= utils.getDocumentConfig(fileEntryId, version, true, renderRequest) %>');
                    docEditor = new DocsAPI.DocEditor("placeholder", config);
                }
            };

            divScriptApi.append(scriptApi);
        })();
    </aui:script>
</div>

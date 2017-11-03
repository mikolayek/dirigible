package org.eclipse.dirigible.runtime.git.service;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.dirigible.api.v3.security.UserFacade;
import org.eclipse.dirigible.commons.api.service.AbstractRestService;
import org.eclipse.dirigible.commons.api.service.IRestService;
import org.eclipse.dirigible.core.git.GitConnectorException;
import org.eclipse.dirigible.runtime.git.model.GitCloneModel;
import org.eclipse.dirigible.runtime.git.model.GitPullModel;
import org.eclipse.dirigible.runtime.git.model.GitPushModel;
import org.eclipse.dirigible.runtime.git.model.GitResetModel;
import org.eclipse.dirigible.runtime.git.model.GitShareModel;
import org.eclipse.dirigible.runtime.git.model.GitUpdateDepenciesModel;
import org.eclipse.dirigible.runtime.git.processor.GitProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

/**
 * Front facing REST service serving the raw repository content
 */
@Singleton
@Path("/ide/git/{workspace}")
@Api(value = "IDE - Git", authorizations = { @Authorization(value = "basicAuth", scopes = {}) })
@ApiResponses({ @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden") })
public class GitRestService extends AbstractRestService implements IRestService {
	
	private static final Logger logger = LoggerFactory.getLogger(GitRestService.class);

	@Inject
	private GitProcessor processor;
	
	@Context
	private HttpServletResponse response;

	@POST
	@Path("/clone")
	@Produces("application/json")
	@ApiOperation("Clone Git Repository")
	@ApiResponses({ @ApiResponse(code = 200, message = "Git Repository Cloned") })
	public Response cloneRepository(@ApiParam(value = "Name of the Workspace", required = true) @PathParam("workspace") String workspace,
			GitCloneModel model) throws GitConnectorException {
		String user = UserFacade.getName();
		if (user == null) {
			sendErrorForbidden(response, NO_LOGGED_IN_USER);
			return Response.status(Status.FORBIDDEN).build();
		}
		processor.clone(workspace, model);
		return Response.ok().build();
	}

	@POST
	@Path("/pull")
	@Produces("application/json")
	@ApiOperation("Pull Git Projects into the Workspace")
	@ApiResponses({ @ApiResponse(code = 200, message = "Git Projects Pulled") })
	public Response pullProjects(@ApiParam(value = "Name of the Workspace", required = true) @PathParam("workspace") String workspace,
			GitPullModel model) {
		String user = UserFacade.getName();
		if (user == null) {
			sendErrorForbidden(response, NO_LOGGED_IN_USER);
			return Response.status(Status.FORBIDDEN).build();
		}
		processor.pull(workspace, model);
		return Response.ok().build();
	}

	@POST
	@Path("/{project}/pull")
	@Produces("application/json")
	@ApiOperation("Pull Git Project into the Workspace")
	@ApiResponses({ @ApiResponse(code = 200, message = "Git Project Pulled") })
	public Response pullProject(@ApiParam(value = "Name of the Workspace", required = true) @PathParam("workspace") String workspace,
			@ApiParam(value = "Name of the Project", required = true) @PathParam("project") String project, GitPullModel model) {
		String user = UserFacade.getName();
		if (user == null) {
			sendErrorForbidden(response, NO_LOGGED_IN_USER);
			return Response.status(Status.FORBIDDEN).build();
		}
		model.setProjects(Arrays.asList(project));
		processor.pull(workspace, model);
		return Response.ok().build();
	}

	@POST
	@Path("/push")
	@Produces("application/json")
	@ApiOperation("Push Git Projects into Git Repository")
	@ApiResponses({ @ApiResponse(code = 200, message = "Git Projects Pushed") })
	public Response pushProjects(@ApiParam(value = "Name of the Workspace", required = true) @PathParam("workspace") String workspace,
			GitPushModel model) {
		String user = UserFacade.getName();
		if (user == null) {
			sendErrorForbidden(response, NO_LOGGED_IN_USER);
			return Response.status(Status.FORBIDDEN).build();
		}
		processor.push(workspace, model);
		return Response.ok().build();
	}

	@POST
	@Path("/{project}/push")
	@Produces("application/json")
	@ApiOperation("Push Git Project into Git Repository")
	@ApiResponses({ @ApiResponse(code = 200, message = "Git Project Pushed") })
	public Response pushProject(@ApiParam(value = "Name of the Workspace", required = true) @PathParam("workspace") String workspace,
			@ApiParam(value = "Name of the Project", required = true) @PathParam("project") String project, GitPushModel model) {
		String user = UserFacade.getName();
		if (user == null) {
			sendErrorForbidden(response, NO_LOGGED_IN_USER);
			return Response.status(Status.FORBIDDEN).build();
		}
		model.setProjects(Arrays.asList(project));
		processor.push(workspace, model);
		return Response.ok().build();
	}

	@POST
	@Path("/reset")
	@Produces("application/json")
	@ApiOperation("Hard Reset Git Projects in the Workspace")
	@ApiResponses({ @ApiResponse(code = 200, message = "Git Projects Reset") })
	public Response resetProjects(@ApiParam(value = "Name of the Workspace", required = true) @PathParam("workspace") String workspace,
			GitResetModel model) {
		String user = UserFacade.getName();
		if (user == null) {
			sendErrorForbidden(response, NO_LOGGED_IN_USER);
			return Response.status(Status.FORBIDDEN).build();
		}
		processor.reset(workspace, model);
		return Response.ok().build();
	}

	@POST
	@Path("/{project}/reset")
	@Produces("application/json")
	@ApiOperation("Hard Reset Git Project in the Workspace")
	@ApiResponses({ @ApiResponse(code = 200, message = "Git Project Reset") })
	public Response resetProject(@ApiParam(value = "Name of the Workspace", required = true) @PathParam("workspace") String workspace,
			@ApiParam(value = "Name of the Project", required = true) @PathParam("project") String project, GitResetModel model) {
		String user = UserFacade.getName();
		if (user == null) {
			sendErrorForbidden(response, NO_LOGGED_IN_USER);
			return Response.status(Status.FORBIDDEN).build();
		}
		model.setProjects(Arrays.asList(project));
		processor.reset(workspace, model);
		return Response.ok().build();
	}

	@POST
	@Path("/{project}/share")
	@Produces("application/json")
	@ApiOperation("Share Git Project into Git Repository")
	@ApiResponses({ @ApiResponse(code = 200, message = "Git Project Shared") })
	public Response shareProject(@ApiParam(value = "Name of the Workspace", required = true) @PathParam("workspace") String workspace,
			@ApiParam(value = "Name of the Project", required = true) @PathParam("project") String project, GitShareModel model) {
		String user = UserFacade.getName();
		if (user == null) {
			sendErrorForbidden(response, NO_LOGGED_IN_USER);
			return Response.status(Status.FORBIDDEN).build();
		}
		model.setProject(project);
		processor.share(workspace, model);
		return Response.ok().build();
	}

	@POST
	@Path("/uppdate-dependencies")
	@Produces("application/json")
	@ApiOperation("Update Git Projects Dependencies")
	@ApiResponses({ @ApiResponse(code = 200, message = "Git Projects Dependencies Updated") })
	public Response updateProjectsDependencies(@ApiParam(value = "Name of the Workspace", required = true) @PathParam("workspace") String workspace,
			GitUpdateDepenciesModel model) throws GitConnectorException {
		String user = UserFacade.getName();
		if (user == null) {
			sendErrorForbidden(response, NO_LOGGED_IN_USER);
			return Response.status(Status.FORBIDDEN).build();
		}
		processor.updateDependencies(workspace, model);
		return Response.ok().build();
	}

	@POST
	@Path("/{project}/uppdate-dependencies")
	@Produces("application/json")
	@ApiOperation("Update Git Project Dependencies")
	@ApiResponses({ @ApiResponse(code = 200, message = "Git Project Dependencies Updated") })
	public Response updateProjectDependencies(@ApiParam(value = "Name of the Workspace", required = true) @PathParam("workspace") String workspace,
			@ApiParam(value = "Name of the Project", required = true) @PathParam("project") String project, GitUpdateDepenciesModel model)
			throws GitConnectorException {
		String user = UserFacade.getName();
		if (user == null) {
			sendErrorForbidden(response, NO_LOGGED_IN_USER);
			return Response.status(Status.FORBIDDEN).build();
		}
		model.setProjects(Arrays.asList(project));
		processor.updateDependencies(workspace, model);
		return Response.ok().build();
	}

	@Override
	public Class<? extends IRestService> getType() {
		return GitRestService.class;
	}
	
	@Override
	protected Logger getLogger() {
		return logger;
	}

}
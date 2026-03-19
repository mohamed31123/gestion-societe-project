package ma.fst.projet2societe.services;

import ma.fst.projet2societe.dto.ProjectRequest;
import ma.fst.projet2societe.dto.ProjectResponse;
import ma.fst.projet2societe.dto.ProjectResume;

import java.util.List;

public interface ProjectService {

    ProjectResponse create(ProjectRequest request);

    ProjectResponse update(Long id, ProjectRequest request);

    ProjectResponse getProject(Long id);

    List<ProjectResponse> getAllProjects();

    ProjectResume getResume(Long id);

    void delete(Long id);
}
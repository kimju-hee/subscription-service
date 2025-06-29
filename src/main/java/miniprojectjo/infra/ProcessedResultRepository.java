package miniprojectjo.infra;

import miniprojectjo.domain.ProcessedResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProcessedResultRepository extends JpaRepository<ProcessedResult, Long> {
    Optional<ProcessedResult> findByUserIdAndBookId(Long userId, Long bookId);
}

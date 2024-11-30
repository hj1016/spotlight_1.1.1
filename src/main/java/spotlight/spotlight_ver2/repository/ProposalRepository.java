package spotlight.spotlight_ver2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spotlight.spotlight_ver2.entity.Proposal;
import spotlight.spotlight_ver2.entity.User;

import java.util.List;
import java.util.Optional;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    List<Proposal> findByStudent_User(User user);
    List<Proposal> findByRecruiter_User(User user);
    Optional<Proposal> findByProposalIdAndStudent_UserId(Long proposalId, Long userId);
    Optional<Proposal> findByProposalIdAndRecruiter_UserId(Long proposalId, Long userId);
}
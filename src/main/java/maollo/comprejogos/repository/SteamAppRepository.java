package maollo.comprejogos.repository;

import maollo.comprejogos.domain.SteamApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SteamAppRepository extends JpaRepository<SteamApp, Long> {
    List<SteamApp> findByNameContainingIgnoreCase(String name);
}

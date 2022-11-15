package com.acme.flotte.service;

import com.acme.flotte.entity.Flottenfahrzeug;
import com.acme.flotte.repository.FlottenfahrzeugRepository;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

/**
 * Anwendungslogik für Flottenfahrzeuge auch mit Bean Validation.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public final class FlottenfahrzeugWriteService {
    private final FlottenfahrzeugRepository repo;

    private final Validator validator;

    /**
     * Ein neues Flottenfahrzeug anlegen.
     *
     * @param flottenfahrzeug Das Objekt des neu anzulegenden Flottenfahrzeugs.
     * @return Das neu angelegte Flottenfahrzeug mit generierter ID
     * @throws ConstraintViolationsException Falls mindestens ein Constraint verletzt ist.
     * @throws EmailExistsException Es gibt bereits einen Kunden mit der Emailadresse.
     */
    public Flottenfahrzeug create(@Valid final Flottenfahrzeug flottenfahrzeug) {
        log.debug("create: {}", flottenfahrzeug);

        final var violations = validator.validate(flottenfahrzeug);
        if (!violations.isEmpty()) {
            log.debug("create: violations={}", violations);
            throw new ConstraintViolationsException(violations);
        }

        if (repo.isEmailExisting(flottenfahrzeug.getEmail())) {
            throw new EmailExistsException(flottenfahrzeug.getEmail());
        }

        final var flottenfahrzeugDB = repo.create(flottenfahrzeug);
        log.debug("create: {}", flottenfahrzeugDB);
        return flottenfahrzeugDB;
    }

    /**
     * Ein vorhandenes Flottenfahrzeug aktualisieren.
     *
     * @param flottenfahrzeug Das Objekt mit den neuen Daten (ohne ID)
     * @param id ID des zu aktualisierenden Flottenfahrzeug
     * @throws ConstraintViolationsException Falls mindestens ein Constraint verletzt ist.
     * @throws NotFoundException Kein Flottenfahrzeug zur ID vorhanden.
     * @throws EmailExistsException Es gibt bereits einen Flottenfahrzeug mit der Emailadresse.
     */
    public void update(final Flottenfahrzeug flottenfahrzeug, final UUID id) {
        log.debug("update: {}", flottenfahrzeug);
        log.debug("update: id={}", id);

        final var violations = validator.validate(flottenfahrzeug);
        if (!violations.isEmpty()) {
            log.debug("update: violations={}", violations);
            throw new ConstraintViolationsException(violations);
        }

        final var kundeDbOptional = repo.findById(id);
        if (kundeDbOptional.isEmpty()) {
            throw new NotFoundException(id);
        }

        final var email = flottenfahrzeug.getEmail();
        final var flottenfahrzeugDB = kundeDbOptional.get();
        // Ist die neue Email bei einem *ANDEREN* Flottenfahrzeug vorhanden?
        if (!Objects.equals(email, flottenfahrzeugDB.getEmail()) && repo.isEmailExisting(email)) {
            log.debug("update: email {} existiert", email);
            throw new EmailExistsException(email);
        }

        flottenfahrzeug.setId(id);
        repo.update(flottenfahrzeug);
    }

}

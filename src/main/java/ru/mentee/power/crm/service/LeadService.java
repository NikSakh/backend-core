package ru.mentee.power.crm.service;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.LeadEntity;
import ru.mentee.power.crm.jparepository.RejectionReasonsRepository;
import ru.mentee.power.crm.model.LeadDto;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;

@Service
public class LeadService {

  private static final Logger LOG = LoggerFactory.getLogger(LeadService.class);

  private final LeadRepository repository;
  private final RejectionReasonsRepository rejectionReasonsRepository;

  public LeadService(
      LeadRepository repository, RejectionReasonsRepository rejectionReasonsRepository) {
    this.repository = repository;
    this.rejectionReasonsRepository = rejectionReasonsRepository;
    LOG.info("LeadService constructor called");
  }

  @PostConstruct
  void init() {
    LOG.info("LeadService @PostConstruct init() called - Bean lifecycle phase");
  }

  public LeadDto addLead(String email, String company, LeadStatus status) {
    if (email == null || email.trim().isEmpty()) {
      throw new IllegalArgumentException("Email cannot be null or empty");
    }
    if (company == null || company.trim().isEmpty()) {
      throw new IllegalArgumentException("Company cannot be null or empty");
    }
    if (status == null) {
      throw new IllegalArgumentException("Status cannot be null");
    }

    String normalizedEmail = email.trim().toLowerCase();

    Optional<LeadEntity> existing = repository.findByEmail(normalizedEmail);
    if (existing.isPresent()) {
      throw new IllegalStateException(
          String.format(
              "Lead with email '%s' already exists (ID: %s)",
              normalizedEmail, existing.get().id().toString()));
    }

    Address address = new Address("Unknown", "Unknown", "00000");
    Contact contact = new Contact(normalizedEmail, "Unknown", address);

    LeadEntity leadEntity =
        new LeadEntity(UUID.randomUUID(), contact, company.trim(), status.name());

    LeadEntity savedEntity = repository.save(leadEntity);

    return convertToDto(savedEntity);
  }

  public List<LeadDto> findAll() {
    List<LeadEntity> entities = repository.findAll();
    return entities.stream().map(this::convertToDto).collect(Collectors.toList());
  }

  public List<LeadDto> findByStatus(LeadStatus status) {
    List<LeadEntity> entities = repository.findAll();
    return entities.stream()
        .filter(lead -> lead.status().equals(status.name()))
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  public Optional<LeadDto> findById(UUID id) {
    Optional<LeadEntity> entity = repository.findById(id.toString());
    return entity.map(this::convertToDto);
  }

  public Optional<LeadDto> findByEmail(String email) {
    Optional<LeadEntity> entity = repository.findByEmail(email.trim().toLowerCase());
    return entity.map(this::convertToDto);
  }

  private LeadDto convertToDto(LeadEntity entity) {
    Contact contact = entity.contact();
    String rejectionReasonName = null;
    if (entity.rejectionReasonId() != null && !entity.rejectionReasonId().isEmpty()) {
      LOG.info("Looking up rejection reason: {}", entity.rejectionReasonId());
      rejectionReasonName =
          rejectionReasonsRepository
              .findById(UUID.fromString(entity.rejectionReasonId()))
              .map(r -> r.getName())
              .orElse(null);
      LOG.info("Found rejection reason name: {}", rejectionReasonName);
    }
    return new LeadDto(
        entity.id().toString(),
        contact.email(),
        contact.phone(),
        entity.company(),
        LeadStatus.valueOf(entity.status()),
        entity.rejectionReasonId(),
        rejectionReasonName);
  }

  public LeadDto update(UUID id, String email, String phone, String company, LeadStatus status) {
    Optional<LeadEntity> existing = repository.findById(id.toString());
    if (existing.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lead not found with id: " + id);
    }

    LeadEntity updatedEntity =
        new LeadEntity(
            id,
            new Contact(email.trim().toLowerCase(), phone, existing.get().contact().address()),
            company.trim(),
            status.name(),
            existing.get().rejectionReasonId());

    repository.save(updatedEntity);

    return convertToDto(updatedEntity);
  }

  public LeadDto updateWithRejectionReason(
      UUID id,
      String email,
      String phone,
      String company,
      LeadStatus status,
      String rejectionReasonId) {
    Optional<LeadEntity> existing = repository.findById(id.toString());
    if (existing.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lead not found with id: " + id);
    }

    LeadEntity updatedEntity =
        new LeadEntity(
            id,
            new Contact(email.trim().toLowerCase(), phone, existing.get().contact().address()),
            company.trim(),
            status.name(),
            rejectionReasonId);

    repository.save(updatedEntity);

    return convertToDto(updatedEntity);
  }

  public void delete(UUID id) {
    Optional<LeadEntity> existing = repository.findById(id.toString());
    if (existing.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lead not found with id: " + id);
    }
    repository.delete(id.toString());
  }

  public List<LeadDto> findLeads(String search, String status) {
    List<LeadEntity> entities = repository.findAll();

    Stream<LeadEntity> stream = entities.stream();

    if (search != null && !search.isEmpty()) {
      String lowerSearch = search.toLowerCase();
      stream =
          stream.filter(
              lead ->
                  lead.contact().email().toLowerCase().contains(lowerSearch)
                      || lead.company().toLowerCase().contains(lowerSearch));
    }

    if (status != null && !status.isEmpty()) {
      stream = stream.filter(lead -> lead.status().equalsIgnoreCase(status));
    }

    return stream.map(this::convertToDto).collect(Collectors.toList());
  }
}

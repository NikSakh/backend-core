package ru.mentee.power.crm.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.LeadEntity;
import ru.mentee.power.crm.model.LeadDto;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;

@Service
public class LeadService {

  private final LeadRepository repository;

  public LeadService(LeadRepository repository) {
    this.repository = repository;
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
          String.format("Lead with email '%s' already exists (ID: %s)",
              normalizedEmail, existing.get().id().toString())
      );
    }

    Address address = new Address("Unknown", "Unknown", "00000");
    Contact contact = new Contact(normalizedEmail, "Unknown", address);

    LeadEntity leadEntity = new LeadEntity(
        UUID.randomUUID(),
        contact,
        company.trim(),
        status.name()
    );

    LeadEntity savedEntity = repository.save(leadEntity);

    return convertToDto(savedEntity);
  }

  public List<LeadDto> findAll() {
    List<LeadEntity> entities = repository.findAll();
    return entities.stream()
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
    return new LeadDto(
        entity.id().toString(),
        contact.email(),
        contact.phone(),
        entity.company(),
        LeadStatus.valueOf(entity.status())
    );
  }
}
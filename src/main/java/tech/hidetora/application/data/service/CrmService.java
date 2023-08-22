package tech.hidetora.application.data.service;

import tech.hidetora.application.data.entity.Company;
import tech.hidetora.application.data.entity.Contact;
import tech.hidetora.application.data.entity.Status;
import tech.hidetora.application.data.repository.CompanyRepository;
import tech.hidetora.application.data.repository.ContactRepository;
import tech.hidetora.application.data.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Hidetora
 * @version 1.0.0
 * @since 2022/04/18
 */

@Service // The @Service annotation makes this a Spring-managed service that you can inject into your view.
@Transactional
@RequiredArgsConstructor // Use Spring constructor injection to autowire the database repositories.
@Slf4j
public class CrmService {
    private final ContactRepository contactRepository;
    private final CompanyRepository companyRepository;
    private final StatusRepository statusRepository;

    public List<Contact> findAllContacts(String stringFilter) {
        // Check if there’s an active filter: return either all contacts,
        // or use the repository to filter based on the string.
        if (stringFilter == null || stringFilter.isEmpty()) {
            log.info("Fetching all contacts");
            return contactRepository.findAll();
        } else {
            log.info("Performing a search for: " + stringFilter);
            return contactRepository.search(stringFilter);
        }
    }

    public long countContacts() {
        return contactRepository.count();
    }

    public void deleteContact(Contact contact) {
        contactRepository.delete(contact);
    }

    public void saveContact(Contact contact) {
        // Service classes often include validation and other business rules before persisting data.
        // You check here that you aren’t trying to save a null object.
        if (contact == null) {
            log.error("Contact is null. Are you sure you have connected your form to the application?");
            System.err.println("Contact is null. Are you sure you have connected your form to the application?");
            return;
        }
        contactRepository.save(contact);
    }

    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }

    public List<Status> findAllStatuses(){
        return statusRepository.findAll();
    }
}

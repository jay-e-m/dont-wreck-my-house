package admin.domain;

import admin.data.DataException;
import admin.data.HostRepository;
import admin.models.Host;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class HostService {

    private final HostRepository hostRepository;

    public HostService(HostRepository hostRepository) {
        this.hostRepository = hostRepository;
    }

    public List<Host> findAll() {
        return hostRepository.findAll();
    }

    public Host findHostByName(String name) {
        return hostRepository.findHostByName(name);
    }

    public List<Host> findHostByState(String state) {
        return hostRepository.findHostsByState(state);
    }

    public Host findHostByID(String id) {
        return hostRepository.findHostByID(id);
    }


    private Result<Host> validate(Host host) {
        Result<Host> result = new Result<>();
        if (host == null) {
            result.addErrorMessage("Host is null.");
            return result;
        }

        if (host.getName() == null || host.getName().trim().length() == 0) {
            result.addErrorMessage("Host name is required.");
        }

        if (host.getEmail() == null || host.getEmail().trim().length() == 0) {
            result.addErrorMessage("Email is required.");
        } else if (!host.getEmail().matches("^(.+)@(.+)$")) {
            result.addErrorMessage("Email is not in a valid format.");
        }

        if (host.getPhoneNumber() == null || host.getPhoneNumber().trim().length() == 0) {
            result.addErrorMessage("Phone number is required.");
        } else if (!host.getPhoneNumber().matches("^\\(\\d{3}\\)\\s\\d{3}\\d{4}$")) {
            result.addErrorMessage("Phone number is not in a valid format.");
        }

        if (host.getAddress() == null || host.getAddress().trim().length() == 0) {
            result.addErrorMessage("Address is required.");
        }

        if (host.getCity() == null || host.getCity().trim().length() == 0) {
            result.addErrorMessage("City is required.");
        }

        if (host.getState() == null || host.getState().trim().length() == 0) {
            result.addErrorMessage("State is required.");
        }

        if (host.getPostalCode() == null || host.getPostalCode().trim().length() == 0) {
            result.addErrorMessage("Postal code is required.");
        } else if (!host.getPostalCode().matches("^\\d{5}$")) {
            result.addErrorMessage("Postal code is not in a valid format.");
        }

        if (host.getStandardRate() == null) {
            result.addErrorMessage("Standard rate is required.");
        } else if (host.getStandardRate().compareTo(BigDecimal.ZERO) < 0) {
            result.addErrorMessage("Standard rate cannot be negative.");
        }

        if (host.getWeekendRate() == null) {
            result.addErrorMessage("Weekend rate is required.");
        } else if (host.getWeekendRate().compareTo(BigDecimal.ZERO) < 0) {
            result.addErrorMessage("Weekend rate cannot be negative.");
        }

        return result;
    }

}

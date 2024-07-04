package com.assisto.virtualassistant;

import com.assisto.virtualassistant.model.Appointment;
import com.assisto.virtualassistant.model.Offer;
import com.assisto.virtualassistant.model.Vehicle;
import com.assisto.virtualassistant.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

@Component
public class TerminalMenu {

    @Autowired
    private ChatGPTService chatGPTService;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private VinDecoderService vinDecoderService;
    @Autowired
    private OilService oilService;
    @Autowired
    private OfferService offerService;
    private static final Scanner scanner;
    private boolean isFirstTime = true;

    static {
        scanner = new Scanner(System.in);
    }

    public TerminalMenu() {
    }

    public void showMenu() {
        while (true) {
            if (isFirstTime) {
                System.out.println("Salut numele meu este Assisto! Asistentul tău virtual. Cu ce te pot ajuta?");
                isFirstTime = false;
            } else {
                System.out.println("Cu ce altceva te pot ajuta azi?");
            }
            System.out.println("1. Programare");
            System.out.println("2. Cere ofertă Schimb Filtre + Ulei");
            System.out.println("3. Suport Tehnic");
            System.out.println("4. Verifică Programare");
            System.out.println("5. Ieșire");
            String option = scanner.nextLine();

            if ("1".equals(option)) {
                handleAppointmentOption(scanner);
            } else if ("2".equals(option)) {
                handleRequestQuote(scanner);
            } else if ("3".equals(option)) {
                 handleTechnicalSupport(scanner);
            } else if ("4".equals(option)) {
                handleCheckAppointment(scanner);
            } else if ("5".equals(option)) {
                System.out.println("O zi frumoasă, te mai așteptăm. La revedere!");
                break;
            } else {
                System.out.println("Opțiune invalidă. Te rog să încerci din nou.");
            }
        }
    }

    private void handleAppointmentOption(Scanner scanner, String firstName, String lastName, String phoneNumber, String chassisNumber) {
        while (true) {
            System.out.println("Te rog să îmi spui data la care dorești să te programezi (format: dd.MM.yyyy sau folosind 'azi', 'maine', 'poimaine', sau 'exit' pentru a reveni la meniul principal):");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit") || "4".equals(input)) return;

            LocalDate date = null;

            switch (input.toLowerCase()) {
                case "azi":
                case "astazi":
                case "astăzi":
                    date = LocalDate.now();
                    break;
                case "maine":
                case "mâine":
                    date = LocalDate.now().plusDays(1);
                    break;
                case "poimaine":
                case "poimâine":
                    date = LocalDate.now().plusDays(2);
                    break;
                default:
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                        date = LocalDate.parse(input, formatter);
                        if (date.isBefore(LocalDate.now())) {
                            System.out.println("Data introdusă este în trecut. Te rog să introduci o dată validă.");
                            continue;
                        }
                    } catch (DateTimeParseException e) {
                        System.out.println("Nu am putut înțelege data. Te rog să încerci din nou.");
                        continue;
                    }
            }

            System.out.println("Data selectată: " + date.format(DateTimeFormatter.ISO_LOCAL_DATE));

            if (firstName == null || lastName == null || phoneNumber == null || chassisNumber == null) {
                System.out.println("Te rog să îmi spui prenumele tău:");
                firstName = scanner.nextLine();

                System.out.println("Te rog să îmi spui numele tău de familie:");
                lastName = scanner.nextLine();

                System.out.println("Te rog să îmi spui numărul tău de telefon:");
                phoneNumber = scanner.nextLine();

                System.out.println("Te rog să îmi spui seria de șasiu a mașinii:");
                chassisNumber = scanner.nextLine();
            }

            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

            List<Appointment> availableSlots = appointmentService.getAvailableSlots(startOfDay, endOfDay);

            if (availableSlots.isEmpty()) {
                System.out.println("Nu sunt sloturi disponibile pentru data selectată. Verificăm pentru următoarele două zile...");

                LocalDateTime nextDayStart = date.plusDays(1).atStartOfDay();
                LocalDateTime nextDayEnd = date.plusDays(1).atTime(LocalTime.MAX);
                List<Appointment> nextDaySlots = appointmentService.getAvailableSlots(nextDayStart, nextDayEnd);

                if (!nextDaySlots.isEmpty()) {
                    System.out.println("Sloturi disponibile pentru data următoare:");
                    for (Appointment slot : nextDaySlots) {
                        System.out.println("ID: " + slot.getId() + " - " + slot.getStartTime().toLocalTime() + " - " + slot.getEndTime().toLocalTime());
                    }
                    selectAndBookSlot(scanner, firstName, lastName, phoneNumber, chassisNumber, availableSlots);
                    return;
                }

                LocalDateTime dayAfterNextStart = date.plusDays(2).atStartOfDay();
                LocalDateTime dayAfterNextEnd = date.plusDays(2).atTime(LocalTime.MAX);
                List<Appointment> dayAfterNextSlots = appointmentService.getAvailableSlots(dayAfterNextStart, dayAfterNextEnd);

                if (!dayAfterNextSlots.isEmpty()) {
                    System.out.println("Sloturi disponibile pentru două zile după data selectată:");
                    for (Appointment slot : dayAfterNextSlots) {
                        System.out.println("ID: " + slot.getId() + " - " + slot.getStartTime().toLocalTime() + " - " + slot.getEndTime().toLocalTime());
                    }
                    selectAndBookSlot(scanner, firstName, lastName, phoneNumber, chassisNumber, availableSlots);
                    return;
                }

                System.out.println("Nu există sloturi disponibile pentru data selectată și următoarele două zile. Te rog să încerci să te programezi pentru altă dată.");
            } else {
                System.out.println("Sloturi disponibile pentru data selectată:");
                for (Appointment slot : availableSlots) {
                    System.out.println("ID: " + slot.getId() + " - " + slot.getStartTime().toLocalTime() + " - " + slot.getEndTime().toLocalTime());
                }
                selectAndBookSlot(scanner, firstName, lastName, phoneNumber, chassisNumber, availableSlots);
                return;
            }
        }
    }

    private void handleAppointmentOption(Scanner scanner) {
        handleAppointmentOption(scanner, null, null, null, null);
    }



    private void selectAndBookSlot(Scanner scanner, String firstName, String lastName, String phoneNumber, String chassisNumber, List<Appointment> availableSlots) {
        System.out.println("Te rog să introduci ID-ul slotului dorit:");
        while (true) {
            try {
                long slotId = Long.parseLong(scanner.nextLine());
                Appointment selectedSlot = availableSlots.stream()
                        .filter(slot -> slot.getId() == slotId)
                        .findFirst()
                        .orElse(null);

                if (selectedSlot != null) {
                    Appointment bookedAppointment = appointmentService.bookSlot(slotId, firstName, lastName, phoneNumber, chassisNumber);

                    System.out.println("Programarea ta a fost realizată cu succes, " + firstName + "! Te-ai programat pentru data " + bookedAppointment.getStartTime().toLocalDate() + " la ora " + bookedAppointment.getStartTime().toLocalTime() + ".");
                    return;
                } else {
                    System.out.println("ID-ul introdus nu este valid. Te rog să încerci din nou.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Te rog să introduci un ID valid.");
            }
        }
    }


    private void handleCheckAppointment(Scanner scanner) {
        System.out.println("Te rog să îmi spui numărul tău de telefon pentru a verifica programările:");
        String phoneNumber = scanner.nextLine().replaceAll("\\s+", ""); // Normalizează numărul de telefon eliminând spațiile

        List<Appointment> appointments = appointmentService.getAppointmentsByPhoneNumber(phoneNumber);

        if (appointments.isEmpty()) {
            System.out.println("Nu există programări pentru numărul de telefon specificat.");
        } else {
            System.out.println("-------------------------");
            System.out.println("Programările tale:");
            for (Appointment appointment : appointments) {
                System.out.println("Data: " + appointment.getStartTime().toLocalDate() + " Ora: " + appointment.getStartTime().toLocalTime());
                System.out.println("-------------------------");
            }
        }
    }

    private void handleRequestQuote(Scanner scanner) {
        System.out.println("Pentru a primi o ofertă pentru schimbul de filtre și ulei, te rog să îmi furnizezi seria de șasiu a mașinii:");
        String vin = scanner.nextLine();

        if (VinDecoderService.checkVIN(vin)) {
            Vehicle vehicle = vinDecoderService.decodeVin(vin);
            if (vehicle != null) {
                System.out.println("\nConform seriei de sasiu, vehiculul este următorul:");
                System.out.println("Marca: " + vehicle.getMake());
                System.out.println("Model: " + vehicle.getModel());
                System.out.println("An: " + vehicle.getYear());
                System.out.println("\nTe rog să îmi spui capacitatea cilindrică a motorului (ex: 2.0 TDI):");
                String engine = scanner.nextLine();
                vehicle.setEngine(engine);
                System.out.println("Te rog să îmi spui tipul de combustibil (ex: diesel, benzină):");
                String fuelType = scanner.nextLine();
                vehicle.setFuelType(fuelType);

                System.out.println("\nTe rog să îmi spui tipul de vâscozitate al uleiului (ex: 5W30):");
                String oilViscosity = scanner.nextLine();

                System.out.println("\nTe rog să îmi spui prenumele tău:");
                String firstName = scanner.nextLine();
                System.out.println("Te rog să îmi spui numele tău de familie:");
                String lastName = scanner.nextLine();
                System.out.println("Te rog să îmi spui numărul tău de telefon:");
                String phoneNumber = scanner.nextLine();

                Offer offer = offerService.createOffer(vin, oilViscosity, firstName, lastName, phoneNumber);

                System.out.println("\nOferta ta a fost generată cu succes! Numărul ofertei este: " + offer.getOfferNumber());
                System.out.println("Detalii ofertă:");
                System.out.println("Ulei: " + offer.getOilType());
                System.out.println("Filtru: " + offer.getFilterBrand());
                System.out.println("Preț total: " + offer.getTotalPrice() + " RON");

                System.out.println("Dorești să faci o programare pentru această ofertă? (da/nu)");
                String response = scanner.nextLine();
                if ("da".equalsIgnoreCase(response)) {
                    handleAppointmentOptionWithDetails(scanner, firstName, lastName, phoneNumber, vin);
                }
            } else {
                System.out.println("Seria de șasiu nu este validă sau nu am putut obține informații despre vehicul. Te rog să încerci din nou.");
            }
        } else {
            System.out.println("Seria de șasiu nu este validă sau nu am putut obține informații despre vehicul. Te rog să încerci din nou.");
        }
    }

    private void handleAppointmentOptionWithDetails(Scanner scanner, String firstName, String lastName, String phoneNumber, String chassisNumber) {
        while (true) {
            System.out.println("Te rog să îmi spui data la care dorești să te programezi (format: dd.MM.yyyy sau folosind 'azi', 'maine', 'poimaine', sau 'exit' pentru a reveni la meniul principal):");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit") || "4".equals(input)) return;

            LocalDate date = null;

            switch (input.toLowerCase()) {
                case "azi":
                case "astazi":
                case "astăzi":
                    date = LocalDate.now();
                    break;
                case "maine":
                case "mâine":
                    date = LocalDate.now().plusDays(1);
                    break;
                case "poimaine":
                case "poimâine":
                    date = LocalDate.now().plusDays(2);
                    break;
                default:
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                        date = LocalDate.parse(input, formatter);
                        if (date.isBefore(LocalDate.now())) {
                            System.out.println("Data introdusă este în trecut. Te rog să introduci o dată validă.");
                            continue;
                        }
                    } catch (DateTimeParseException e) {
                        System.out.println("Nu am putut înțelege data. Te rog să încerci din nou.");
                        continue;
                    }
            }

            System.out.println("Data selectată: " + date.format(DateTimeFormatter.ISO_LOCAL_DATE));

            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

            List<Appointment> availableSlots = appointmentService.getAvailableSlots(startOfDay, endOfDay);

            if (availableSlots.isEmpty()) {
                System.out.println("Nu sunt sloturi disponibile pentru data selectată. Verificăm pentru următoarele două zile...");

                LocalDateTime nextDayStart = date.plusDays(1).atStartOfDay();
                LocalDateTime nextDayEnd = date.plusDays(1).atTime(LocalTime.MAX);
                List<Appointment> nextDaySlots = appointmentService.getAvailableSlots(nextDayStart, nextDayEnd);

                if (!nextDaySlots.isEmpty()) {
                    System.out.println("Sloturi disponibile pentru data următoare:");
                    for (Appointment slot : nextDaySlots) {
                        System.out.println("ID: " + slot.getId() + " - " + slot.getStartTime().toLocalTime() + " - " + slot.getEndTime().toLocalTime());
                    }
                    selectAndBookSlot(scanner, firstName, lastName, phoneNumber, chassisNumber, nextDaySlots);
                    return;
                }

                LocalDateTime dayAfterNextStart = date.plusDays(2).atStartOfDay();
                LocalDateTime dayAfterNextEnd = date.plusDays(2).atTime(LocalTime.MAX);
                List<Appointment> dayAfterNextSlots = appointmentService.getAvailableSlots(dayAfterNextStart, dayAfterNextEnd);

                if (!dayAfterNextSlots.isEmpty()) {
                    System.out.println("Sloturi disponibile pentru două zile după data selectată:");
                    for (Appointment slot : dayAfterNextSlots) {
                        System.out.println("ID: " + slot.getId() + " - " + slot.getStartTime().toLocalTime() + " - " + slot.getEndTime().toLocalTime());
                    }
                    selectAndBookSlot(scanner, firstName, lastName, phoneNumber, chassisNumber, dayAfterNextSlots);
                    return;
                }

                System.out.println("Nu există sloturi disponibile pentru data selectată și următoarele două zile. Te rog să încerci să te programezi pentru altă dată.");
            } else {
                System.out.println("Sloturi disponibile pentru data selectată:");
                for (Appointment slot : availableSlots) {
                    System.out.println("ID: " + slot.getId() + " - " + slot.getStartTime().toLocalTime() + " - " + slot.getEndTime().toLocalTime());
                }
                selectAndBookSlot(scanner, firstName, lastName, phoneNumber, chassisNumber, availableSlots);
                return;
            }
        }
    }




    private void generateOilFilterChangeQuote(Vehicle vehicle) {

        System.out.println("Generăm oferta pentru schimbul de filtre și ulei pentru vehiculul tău:");
        System.out.println();
        System.out.println("---------------------");
        System.out.println("Marca: " + vehicle.getMake());
        System.out.println("Model: " + vehicle.getModel());
        System.out.println("Anul: " + vehicle.getYear());
        System.out.println("Combustibil: " + vehicle.getFuelType());
        System.out.println("Capacitate cilindrică: " + vehicle.getEngine());
        System.out.println("---------------------");

        double price = calculatePrice(vehicle);
        System.out.println("Preț estimat pentru schimbul de filtre și ulei: " + price + " RON");
        System.out.println();
    }

    private double calculatePrice(Vehicle vehicle) {
        double basePrice = 600;
        if ("Diesel".equalsIgnoreCase(vehicle.getFuelType())) {
            basePrice += 50;
        }
        return basePrice;
    }

    private void handleTechnicalSupport(Scanner scanner) {
        System.out.println("Bine ai venit la suportul tehnic! Te rog să introduci întrebarea ta:");
        while (true) {
            String userInput = scanner.nextLine();
            if (userInput.equalsIgnoreCase("exit") || userInput.equalsIgnoreCase("back")) {
                System.out.println("Te-ai întors la meniul principal.");
                break;
            }
            String response = chatGPTService.getChatGPTResponse(userInput);
            System.out.println("Răspunsul asistentului virtual: " + response);
            System.out.println("Ai altă întrebare? (scrie 'exit' sau 'back' pentru a reveni la meniul principal)");
        }
    }

    private Vehicle mapToVehicle(String vin, VinDecoderResponse vinDecoderResponse) {
        Vehicle vehicle = new Vehicle();
        vehicle.setVin(vin);

        List<VinDecoderResponse.Decode> decodeList = vinDecoderResponse.getDecode();
        for (VinDecoderResponse.Decode decode : decodeList) {
            String value = vinDecoderService.extractValue(decode.getValue());
            switch (decode.getLabel()) {
                case "Make":
                    vehicle.setMake(value);
                    break;
                case "Model":
                    vehicle.setModel(value);
                    break;
                case "Model Year":
                    vehicle.setYear(value);
                    break;
                case "Engine":
                    vehicle.setEngine(value);
                    break;
                case "Fuel Type":
                    vehicle.setFuelType(value);
                    break;
            }
        }

        return vehicle;
    }

    private String generateControlSum(String vin, String id, String apiKey, String secretKey) {
        try {
            String input = vin + "|" + id + "|" + apiKey + "|" + secretKey;
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] hashBytes = messageDigest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hashString = new StringBuilder();
            for (byte b : hashBytes) {
                hashString.append(String.format("%02x", b));
            }
            return hashString.substring(0, 10);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating control sum", e);
        }
    }

    public static boolean checkVIN(String vin) {
        return vin != null && vin.length() == 17;
    }

    public String buildReadableMessage(Vehicle vehicle) {
        return String.format("Marca: %s\nModel: %s\nAnul: %s\nMotor: %s\nCombustibil: %s",
                vehicle.getMake(), vehicle.getModel(), vehicle.getYear(), vehicle.getEngine(), vehicle.getFuelType());
    }

}


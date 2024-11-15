import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.DateTimeException;
import java.util.Scanner;

public class MiniProject {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the input type:");
        System.out.println("1. DOB (Date of Birth)");
        System.out.println("2. AGE (Age)");
        int inputChoice = scanner.nextInt();
        scanner.nextLine();

        String input = "";
        switch (inputChoice) {
            case 1:
                System.out.print("Enter DOB in format (e.g., DOB=28-02-2001): ");
                input = scanner.nextLine();
                break;
            case 2:
                System.out.print("Enter Age in format (e.g., AGE=19-10-0019): ");
                System.out.print("Enter Age in format (e.g., AGE=19-10-0019), DAYS/MONTH/YEAR: ");
                input = scanner.nextLine();
                break;
            default:
                System.out.println("Invalid choice! Exiting.");
                return;
        }
        System.out.print("Enter current/reference date (e.g., 27-02-2024): ");
        String referenceDateStr = scanner.nextLine();
        System.out.println("Choose the date format:");
        System.out.println("1. DD-MM-YYYY");
        System.out.println("2. MM-DD-YYYY");
        System.out.println("3. YYYY-MM-DD");
        int formatChoice = scanner.nextInt();
        scanner.nextLine();

        String dateFormat = "";
        switch (formatChoice) {
            case 1:
                dateFormat = "dd-MM-yyyy";
                break;
            case 2:
                dateFormat = "MM-dd-yyyy";
                break;
            case 3:
                dateFormat = "yyyy-MM-dd";
                break;
            default:
                System.out.println("Invalid format choice! Exiting.");
                return;
        }
        System.out.println("Choose the delimiter:");
        System.out.println("1. '-' (dash)");
        System.out.println("2. '/' (slash)");
        System.out.println("3. '.' (dot)");
        int delimiterChoice = scanner.nextInt();
        scanner.nextLine();

        String delimiter = "";
        switch (delimiterChoice) {
            case 1:
                delimiter = "-";
                break;
            case 2:
                delimiter = "/";
                break;
            case 3:
                delimiter = ".";
                break;
            default:
                System.out.println("Invalid delimiter choice! Exiting.");
                return;
        }


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat.replace("-", delimiter));

        LocalDate referenceDate;
        try {
            referenceDate = LocalDate.parse(referenceDateStr, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid reference date format: " + referenceDateStr);
            return;
        }


        if (input.startsWith("DOB=")) {
            processDOB(input, formatter, referenceDate);
        } else if (input.startsWith("AGE=")) {
            processAGE(input, formatter, referenceDate);
        } else {
            System.out.println("Invalid input type! Must start with 'DOB=' or 'AGE='");
        }
    }


    private static void processDOB(String input, DateTimeFormatter formatter, LocalDate referenceDate) {
        String dobStr = input.substring(4);
        try {
            LocalDate dob = parseAndValidateDate(dobStr, formatter);


            if (dob.isAfter(referenceDate)) {
                System.out.println("Date of birth cannot be in the future.");
                return;
            }

            String age = calculateExactAge(dob, referenceDate);
            System.out.println(age);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid DOB format: " + dobStr);
        } catch (DateTimeException e) {
            System.out.println("Invalid date: " + e.getMessage());
        }
    }


    private static void processAGE(String input, DateTimeFormatter formatter, LocalDate referenceDate) {
        String ageStr = input.substring(4);
        try {
            String[] ageParts = ageStr.split("-");
            if (ageParts.length != 3) {
                System.out.println("Invalid AGE format.");
                return;
            }

            int years = Integer.parseInt(ageParts[2]);
            int months = Integer.parseInt(ageParts[1]);
            int days = Integer.parseInt(ageParts[0]);


            LocalDate dob = referenceDate.minusYears(years).minusMonths(months).minusDays(days);
            if (dob.isAfter(referenceDate)) {
                System.out.println("Calculated Date of Birth cannot be in the future.");
                return;
            }

            validateDate(dob);
            System.out.println("Date of Birth is: " + dob.format(formatter));
        } catch (Exception e) {
            System.out.println("Invalid AGE format: " + ageStr);
        }
    }

    private static LocalDate parseAndValidateDate(String dateStr, DateTimeFormatter formatter) throws DateTimeParseException, DateTimeException {
        LocalDate date = LocalDate.parse(dateStr, formatter);

        validateDate(date);

        return date;
    }

    private static String calculateExactAge(LocalDate dob, LocalDate referenceDate) {
        int years = referenceDate.getYear() - dob.getYear();
        int months = referenceDate.getMonthValue() - dob.getMonthValue();
        int days = referenceDate.getDayOfMonth() - dob.getDayOfMonth();

        if (days < 0) {
            months--;
            days += referenceDate.minusMonths(1).lengthOfMonth();
        }

        if (months < 0) {
            years--;
            months += 12;
        }

        return "Age is: " + years + " years, " + months + " months, " + days + " days";
    }

    private static void validateDate(LocalDate date) throws DateTimeException {
        int day = date.getDayOfMonth();
        int month = date.getMonthValue();

        if (month == 2) { // February
            if (day < 1 || day > 29) {
                throw new DateTimeException("Invalid day for February: " + day);
            }
            if (day == 29 && !date.isLeapYear()) {
                throw new DateTimeException("February 29 is only valid in a leap year.");
            }
        } else if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) {
            throw new DateTimeException("Invalid day for month " + month + ": " + day);
        } else if (day < 1 || day > 31) {
            throw new DateTimeException("Invalid day: " + day);
        }
    }
}

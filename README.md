# Parking Lot Management System

A console-based Java application that simulates a real-world parking lot — vehicle entry, spot allocation, fee calculation, payment, and exit — built with clean object-oriented design and thread-safe spot allocation.

## Features
- Configure a parking lot with custom numbers of Small, Medium, and Large spots
- Configurable hourly tariff with a minimum charge
- Vehicle entry with automatic best-fit spot allocation
- Ticket issuance with entry timestamp
- Fee calculation based on duration (hourly, rounded up, with minimum charge enforced)
- Payment processing (Cash/Card/UPI) with receipt generation
- Vehicle exit that frees the spot and closes the ticket
- Live occupancy snapshot showing occupied/free spots

## Spot Allocation Logic
- **Small vehicle** → fits Small, Medium, or Large spots
- **Medium vehicle** → fits Medium or Large spots
- **Large vehicle** → fits only Large spots

The system automatically finds the smallest suitable free spot for each vehicle.

## Fee Calculation
- Duration is rounded up to the nearest hour
- Fee = hours × hourly rate
- Final fee = `max(calculated fee, minimum charge)`

## Project Structure
ParkingApp.java       → Console menu & application entry point
ParkingLot.java        → Core engine — spot management, ticketing, payments, occupancy
ParkingSpot.java       → Represents a single spot (thread-safe assign/release)
Ticket.java             → Tracks vehicle, spot, entry/exit time, payment status
Tariff.java             → Hourly rate + minimum charge fee calculation
Payment.java            → Payment record & receipt generation
Vehicle.java            → Vehicle details and required spot type
ExitGate.java           → Handles vehicle exit processing
IdGenerator.java        → Thread-safe unique ID generator for tickets/payments
SpotType.java           → Enum: SMALL, MEDIUM, LARGE
VehicleType.java        → Enum: SMALL, MEDIUM, LARGE

## Tech Stack
- Java (Core OOP, `java.time` API, `Optional`, collections)

## How to Run
1. Clone the repository
git clone <repo-url>
2. Compile the source files
javac parking/*.java
3. Run the application
java parking.ParkingApp

## Menu Options

Configure Lot & Spots        → Set lot name, spot counts, and tariff
Vehicle Entry (Issue Ticket) → Allocates a spot and issues a ticket
Vehicle Exit (Calculate Fee & Pay) → Computes fee, accepts payment, frees spot
Display Occupancy            → Shows current occupied/free spots
Exit


## Sample Flow
1. Configure lot → e.g. 5 Small, 5 Medium, 2 Large spots, ₹30/hr, ₹10 minimum
2. Vehicle entry → enter reg number and size → ticket issued with entry time
3. Vehicle exit → enter ticket ID → view fee breakdown → pay → spot freed

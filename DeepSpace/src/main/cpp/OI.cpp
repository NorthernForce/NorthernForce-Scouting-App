/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

#include "OI.h"

#include "RobotMap.h"

#include <frc/shuffleboard/Shuffleboard.h>
#include <frc/buttons/JoystickButton.h>

// Command inclusions
#include "commands/IntakeCargo.h"
#include "commands/EjectCargo.h"
#include "commands/ElevatorLower.h"
#include "commands/ElevatorRaise.h"
#include "commands/ElevatorStop.h"

// Functions to simplify button mapping.
static void WhenPressed(std::shared_ptr<frc::GenericHID> joystick, int button, frc::Command* command) {
  auto joystickButton = new frc::JoystickButton(joystick.get(), button);
  joystickButton->WhenPressed(command);
}

static void WhenReleased(std::shared_ptr<frc::GenericHID> joystick, int button, frc::Command* command) {
  auto joystickButton = new frc::JoystickButton(joystick.get(), button);
  joystickButton->WhenReleased(command);
}

static void WhileHeld(std::shared_ptr<frc::GenericHID> joystick, int button, frc::Command* command) {
  auto joystickButton = new frc::JoystickButton(joystick.get(), button);
  joystickButton->WhileHeld(command);
}

OI::OI() {
  // Initialize the controllers
  m_driverController.reset(new frc::XboxController(RobotMap::OI::k_driverController_id));
  m_manipulatorController1.reset(new frc::Joystick(RobotMap::OI::k_manipulatorController1_id));
  m_manipulatorController2.reset(new frc::Joystick(RobotMap::OI::k_manipulatorController2_id));

  frc::ShuffleboardTab& basicCommandsTab = frc::Shuffleboard::GetTab("Basic Commands");

  frc::ShuffleboardLayout& cargoLayout = basicCommandsTab.GetLayout("Cargo", "List Layout");
  cargoLayout.Add("IntakeCargo", new IntakeCargo());
  cargoLayout.Add("EjectCargo", new EjectCargo());
  cargoLayout.Add("ElevatorRaise", new ElevatorRaise());
  cargoLayout.Add("ElevatorLower", new ElevatorLower());

  WhileHeld(m_manipulatorController1, 3, new IntakeCargo());
  WhileHeld(m_manipulatorController1, 6, new ElevatorRaise());
  WhileHeld(m_manipulatorController1, 7, new ElevatorLower());
  // WhileHeld(m_manipulatorController1, , new EjectCargo());

  WhenReleased(m_manipulatorController1, 6, new ElevatorStop());
  WhenReleased(m_manipulatorController1, 7, new ElevatorStop());
  // m_driverController->SetRumble(frc::GenericHID::kLeftRumble, 1.0);
}

std::pair<double, double> OI::getSteeringControls() {
  double speed = m_driverController->GetY(frc::XboxController::JoystickHand::kLeftHand) * -1;
  double rotation = m_driverController->GetX(frc::XboxController::JoystickHand::kRightHand);

  return std::make_pair(speed, rotation);
}

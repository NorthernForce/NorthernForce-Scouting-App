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
#include "commands/SetupRobot.h"
#include "commands/ElevatorCalibrate.h"
#include "commands/ElevatorExtend.h"
#include "commands/ToggleClawRaise.h"
#include "commands/CloseClaw.h"
#include "commands/OpenClaw.h"
#include "commands/RaiseClaw.h"
#include "commands/LowerClaw.h"
#include "commands/ElevatorSetPosition.h"
#include "commands/SetupRobot.h"
#include "commands/RetractClimber.h"
#include "commands/PlatformDrive.h"
#include "commands/ClimbStage1.h"
#include "commands/ClimberDriveStop.h"
#include "commands/LowerClimber.h"
#include "commands/ExtendClimber.h"
#include "commands/PositionSetups/SetupForCargoDepositLevel1.h"
#include "commands/PositionSetups/SetupForCargoDepositLevel2.h"
#include "commands/PositionSetups/SetupForCargoDepositLevel3.h"
#include "commands/PositionSetups/SetupForCargoIntake.h"
#include "commands/PositionSetups/SetupForHatchDepositLevel1.h"
#include "commands/PositionSetups/SetupForHatchDepositLevel2.h"
#include "commands/PositionSetups/SetupForHatchDepositLevel3.h"

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

  // frc::ShuffleboardTab& basicCommandsTab = frc::Shuffleboard::GetTab("Basic Commands");
  // basicCommandsTab.Add("SetupRobot", new SetupRobot());
  // basicCommandsTab.Add("Calibrate Robot", new ElevatorCalibrate());

  // frc::ShuffleboardLayout& cargoLayout = basicCommandsTab.GetLayout("Cargo", "List Layout");
  // cargoLayout.Add("IntakeCargo", new IntakeCargo());
  // cargoLayout.Add("EjectCargo", new EjectCargo());
  // cargoLayout.Add("ElevatorRaise", new ElevatorRaise());
  // cargoLayout.Add("ElevatorLower", new ElevatorLower());

  WhileHeld(m_manipulatorController1, 1, new IntakeCargo());
  WhenPressed(m_manipulatorController1, 1, new CloseClaw());
  WhenReleased(m_manipulatorController1, 1, new OpenClaw());

  WhileHeld(m_manipulatorController1, 3, new EjectCargo());
  
  WhenPressed(m_manipulatorController1, 8, new ToggleClawRaise());

  WhenPressed(m_manipulatorController1, 7, new SetupForCargoDepositLevel1());
  WhenPressed(m_manipulatorController1, 11, new SetupForCargoDepositLevel2());
  WhenPressed(m_manipulatorController1, 10, new SetupForCargoDepositLevel3());
  
  WhenPressed(m_manipulatorController1, 4, new SetupForHatchDepositLevel1());
  WhenPressed(m_manipulatorController1, 2, new SetupForHatchDepositLevel2());
  WhenPressed(m_manipulatorController1, 5, new SetupForHatchDepositLevel3());

  WhenPressed(m_manipulatorController1, 9, new ElevatorSetPosition(ElevatorSetPosition::Position::HomePosition));
  WhenPressed(m_manipulatorController1, 6, new SetupForCargoIntake());

  WhenPressed(m_manipulatorController2, 8, new SetupRobot());

  WhileHeld(m_manipulatorController2, 10, new ElevatorRaise());
  WhileHeld(m_manipulatorController2, 11, new ElevatorLower());

  WhenReleased(m_manipulatorController2, 10, new ElevatorStop());
  WhenReleased(m_manipulatorController2, 11, new ElevatorStop());

  WhenPressed(m_manipulatorController2, 4, new ElevatorExtend());
  WhenPressed(m_manipulatorController2, 5, new ElevatorCalibrate());

  WhileHeld(m_manipulatorController2, 6, new ExtendClimber());
  WhileHeld(m_manipulatorController2, 7, new RetractClimber());

  WhileHeld(m_manipulatorController2, 3, new PlatformDrive());
  WhenReleased(m_manipulatorController2, 3, new ClimberDriveStop());

  // WhenPressed(m_manipulatorController2, 6, new ClimbStage1());

  // m_driverController->SetRumble(frc::GenericHID::kLeftRumble, 1.0);
}

std::pair<double, double> OI::getSteeringControls() {
  double speed = m_driverController->GetY(frc::XboxController::JoystickHand::kLeftHand) * -1;
  double rotation = m_driverController->GetX(frc::XboxController::JoystickHand::kRightHand) * 0.85;

  return std::make_pair(speed, rotation);
}
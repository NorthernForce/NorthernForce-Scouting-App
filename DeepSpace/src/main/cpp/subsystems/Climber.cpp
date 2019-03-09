
#include "subsystems/Climber.h"
#include "RobotMap.h"
#include <iostream>

Climber::Climber() : Subsystem("Climber") {

    m_masterTalonLifter.reset(new WPI_TalonSRX (RobotMap::Climber::k_leftClimbingMotor_id));
    m_slaveTalonLifter.reset(new WPI_TalonSRX (RobotMap::Climber::k_rightClimbingMotor_id));

    m_slaveTalonLifter->SetInverted(true);
    m_slaveTalonLifter->Follow(*m_masterTalonLifter);

    m_masterTalonLifter->ConfigForwardLimitSwitchSource(LimitSwitchSource_FeedbackConnector, LimitSwitchNormal_NormallyOpen, 0);
    m_masterTalonLifter->ConfigReverseLimitSwitchSource(LimitSwitchSource_FeedbackConnector, LimitSwitchNormal_NormallyOpen, 0);

    m_masterTalonWheels.reset(new WPI_TalonSRX (RobotMap::Climber::k_driveMotor_id));
}

void Climber::Lower() {
    m_masterTalonLifter->Set(RobotMap::Climber::k_reverseMotorSpeed);
}

void Climber::Raise() {
    m_masterTalonLifter->Set(RobotMap::Climber::k_forwardMotorSpeed);
}

void Climber::Stop() {
    m_masterTalonLifter->Set(0.0);
}

void Climber::DriveForward() {
    m_masterTalonWheels->Set(RobotMap::Climber::k_driveForwardMotorSpeed);
}

void Climber::DriveBackward() {
    m_masterTalonWheels->Set(RobotMap::Climber::k_driveBackwardMotorSpeed);
}

void Climber::DriveStop() {
    m_masterTalonWheels->Set(0.0);
}

bool Climber::AtUpperLimit() {
    return m_masterTalonLifter->GetSensorCollection().IsFwdLimitSwitchClosed(); // might have to switch rev and fwd
}

bool Climber::AtLowerLimit() {
    return m_masterTalonLifter->GetSensorCollection().IsRevLimitSwitchClosed();
}

void Climber::LimitCurrent(WPI_TalonSRX& controller) {
    controller.ConfigPeakCurrentLimit(k_peakCurrent, k_timeout);
    controller.ConfigContinuousCurrentLimit(k_continuousCurrent, k_timeout);
    controller.ConfigPeakCurrentDuration(k_peakCurrentDuration, k_timeout);
    controller.EnableCurrentLimit(true);
} 
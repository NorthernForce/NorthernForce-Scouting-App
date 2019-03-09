
#pragma once
#include "frc/WPILib.h"
#include "ctre/Phoenix.h"

class Climber: public frc::Subsystem
{
public:
	Climber();
	void Lower();
	void Raise();
    void Stop();
    void DriveForward();
    void DriveBackward();
    void DriveStop();
    void LimitCurrent(WPI_TalonSRX&);
    bool AtLowerLimit();
    bool AtUpperLimit();
    
private:
    std::shared_ptr<WPI_TalonSRX> m_masterTalonLifter;
	std::shared_ptr<WPI_TalonSRX> m_slaveTalonLifter;
    std::shared_ptr<WPI_TalonSRX> m_masterTalonWheels;

    constexpr static auto k_timeout = 0;
    constexpr static auto k_peakCurrent = 22;
    constexpr static auto k_continuousCurrent = 11;
    constexpr static auto k_peakCurrentDuration = 2000;

};
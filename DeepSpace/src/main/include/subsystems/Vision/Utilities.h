#pragma once

#include <opencv2/opencv.hpp>

namespace Vision {
  class Utilities {
   public:
    static cv::Point CalcAvgPoint(cv::Point a, cv::Point b);
    static double CalcLineLength(cv::Point a, cv::Point b);
    static double CalcLineAngle(cv::Point origin, cv::Point outer);
    static double CalcLineAngleDeg(cv::Point origin, cv::Point outer);
  };
}
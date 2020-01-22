#pragma once

#include "CatboostModel.h"

#include <cstdint>
#include <vector>

namespace chesslib {

class CatboostEvaluator {
public:
    explicit CatboostEvaluator(CatboostModel model);

    double applyModel(const std::vector<float>& floatFeatures);

private:
    void binariseFeatures(const std::vector<float>& floatFeatures);
    double accumulateTreeValues() const;

private:
    std::vector<float> features_;
    std::vector<uint8_t> binaryFeatures_;
    CatboostModel model_;
};

}

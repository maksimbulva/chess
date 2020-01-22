#pragma once

#include <vector>

namespace chesslib {

struct CatboostModel {
    CatboostModel(
        unsigned int floatFeatureCount,
        unsigned int binaryFeatureCount,
        std::vector<size_t> treeDepth,
        std::vector<unsigned int> treeSplits,
        std::vector<unsigned int> borderCounts,
        std::vector<float> borders,
        std::vector<double> leafValues,
        double scale,
        double bias);

    const unsigned int floatFeatureCount;
    const unsigned int binaryFeatureCount;
    const std::vector<size_t> treeDepth;
    const std::vector<unsigned int> treeSplits;
    const std::vector<unsigned int> borderCounts;
    const std::vector<float> borders;
    const std::vector<double> leafValues;
    const double scale;
    const double bias;
};

}
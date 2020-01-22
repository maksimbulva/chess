#include "CatboostModel.h"

namespace chesslib {

CatboostModel::CatboostModel(
    unsigned int inFloatFeatureCount,
    unsigned int inBinaryFeatureCount,
    std::vector<size_t> inTreeDepth,
    std::vector<unsigned int> inTreeSplits,
    std::vector<unsigned int> inBorderCounts,
    std::vector<float> inBorders,
    std::vector<double> inLeafValues,
    double inScale,
    double inBias)
    : floatFeatureCount(inFloatFeatureCount)
    , binaryFeatureCount(inBinaryFeatureCount)
    , treeDepth(inTreeDepth)
    , treeSplits(inTreeSplits)
    , borderCounts(std::move(inBorderCounts))
    , borders(std::move(inBorders))
    , leafValues(std::move(inLeafValues))
    , scale(inScale)
    , bias(inBias)
{
}

}

#include "jni_utils.h"

namespace chesslib {

void logError(const std::string& message)
{
    __android_log_print(ANDROID_LOG_ERROR, "ChessLib", "%s", message.c_str());
}

}

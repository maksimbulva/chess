#pragma once

#include <jni.h>
#include <android/log.h>

#include <string>

namespace chesslib {

void logError(const std::string& message);

class JavaStringWrapper {
public:
    JavaStringWrapper(JNIEnv* env, jstring javaString)
        : jenv_(env)
        , javaString_(javaString)
        , cstr_(env->GetStringUTFChars(javaString, nullptr))
        , str_(cstr_)
    {
    }

    JavaStringWrapper(const JavaStringWrapper&) = delete;

    ~JavaStringWrapper()
    {
        jenv_->ReleaseStringUTFChars(javaString_, cstr_);
    }

    const std::string& getString() const
    {
        return str_;
    }

private:
    JNIEnv* const jenv_;
    jstring javaString_;
    const char* cstr_;
    std::string str_;
};

}

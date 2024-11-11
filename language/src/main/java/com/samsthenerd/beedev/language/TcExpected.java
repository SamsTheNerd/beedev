package com.samsthenerd.beedev.language;

import com.samsthenerd.beedev.language.TcExpected.TcCheck;
import com.samsthenerd.beedev.language.TcExpected.TcInfer;
import com.samsthenerd.beedev.language.sorts.FType;
import com.samsthenerd.beedev.language.types.FMetaVar;

public sealed interface TcExpected permits TcInfer, TcCheck {
    record TcInfer(FMetaVar meta) implements  TcExpected{};

    record TcCheck(FType type) implements TcExpected{};
}

package com.re.reverb.androidBackend;

public enum DatabaseResponse
{
    SUCCESS(100),
    FAILURE(600);

    public final int value;

    DatabaseResponse(int value)
    {
        this.value = value;
    }
}

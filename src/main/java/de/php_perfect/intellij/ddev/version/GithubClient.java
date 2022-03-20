package de.php_perfect.intellij.ddev.version;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.util.io.HttpRequests;
import com.intellij.util.io.RequestBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public final class GithubClient implements ReleaseClient {
    private final static @NotNull String RELEASE_URL = "https://github.com/drud/ddev/releases/latest";

    private static final Logger LOG = Logger.getInstance(GithubClient.class);

    @Override
    public @Nullable LatestRelease loadCurrentVersion(@NotNull ProgressIndicator indicator) {
        final RequestBuilder requestBuilder = HttpRequests.request(RELEASE_URL).accept("application/json").redirectLimit(1);
        indicator.checkCanceled();

        try {
            LOG.info("Loading latest DDEV release meta data from GitHub");
            return createParser().fromJson(requestBuilder.readString(indicator), LatestRelease.class);
        } catch (IOException e) {
            LOG.error(e);
            return null;
        }
    }

    private Gson createParser() {
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }
}

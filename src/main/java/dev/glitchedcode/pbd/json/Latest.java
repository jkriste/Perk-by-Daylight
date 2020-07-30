package dev.glitchedcode.pbd.json;

import javax.annotation.Nonnull;

public class Latest {

    private String[] notes;
    private Version version;
    private String github_url;
    private Version req_version;
    private Download[] downloads;

    public Download[] getDownloads() {
        return downloads;
    }

    public String getGithubUrl() {
        return github_url;
    }

    public String[] getNotes() {
        return notes;
    }

    public Version getVersion() {
        return version;
    }

    public Version getRequiredVersion() {
        return req_version;
    }

    public static class Download {

        private long size;
        private String name;
        private String file_type;
        private String download_url;

    }

    public static class Version implements Comparable<Version> {

        private int major;
        private int minor;
        private int patch;

        public Version(int major, int minor, int patch) {
            this.major = major;
            this.minor = minor;
            this.patch = patch;
        }

        public int getMajor() {
            return major;
        }

        public int getMinor() {
            return minor;
        }

        public int getPatch() {
            return patch;
        }

        public boolean isValid() {
            return major > -1 && minor > -1 && patch > -1;
        }

        @Override
        public String toString() {
            return "v" + major + "." + minor + "." + patch;
        }

        @Override
        public int compareTo(@Nonnull Version version) {
            return 0; // todo
        }
    }
}
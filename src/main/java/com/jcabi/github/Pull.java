/**
 * Copyright (c) 2012-2013, JCabi.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the jcabi.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jcabi.github;

import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Loggable;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import javax.json.Json;
import javax.json.JsonObject;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Github pull request.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.3
 * @see <a href="http://developer.github.com/v3/pulls/">Pull Request API</a>
 */
@Immutable
@SuppressWarnings("PMD.TooManyMethods")
public interface Pull extends Comparable<Pull> {

    /**
     * Repository we're in.
     * @return Repo
     */
    @NotNull(message = "repository is never NULL")
    Repo repo();

    /**
     * Get its number.
     * @return Pull request number
     */
    int number();

    /**
     * Get all commits of the pull request.
     * @return Commits
     * @throws IOException If fails
     * @see <a href="http://developer.github.com/v3/pulls/#list-commits-on-a-pull-request">List Commits on a Pull Request</a>
     */
    @NotNull(message = "commits are never NULL")
    Iterable<Commit> commits() throws IOException;

    /**
     * List all files of the pull request.
     * @return Files
     * @throws IOException If fails
     * @see <a href="http://developer.github.com/v3/pulls/#list-pull-requests-files">List Pull Request Files</a>
     */
    @NotNull(message = "iterable of files is never NULL")
    Iterable<JsonObject> files() throws IOException;

    /**
     * Merge it.
     * @param msg Commit message
     * @throws IOException If fails
     * @see <a href="http://developer.github.com/v3/pulls/#merge-a-pull-request-merge-buttontrade">Merge a Pull Request</a>
     */
    void merge(@NotNull(message = "message can't be NULL") String msg)
        throws IOException;

    /**
     * Describe it in a JSON object.
     * @return JSON object
     * @throws IOException If fails
     * @see <a href="http://developer.github.com/v3/pulls/#get-a-single-pull-request">Get a Single Pull Request</a>
     */
    @NotNull(message = "JSON is never NULL")
    JsonObject json() throws IOException;

    /**
     * Patch using this JSON object.
     * @param json JSON object
     * @throws IOException If fails
     * @see <a href="http://developer.github.com/v3/pulls/#update-a-pull-request">Update a Pull Request</a>
     */
    void patch(@NotNull(message = "JSON can't be NULL") JsonObject json)
        throws IOException;

    /**
     * Pull request manipulation toolkit.
     */
    @Immutable
    @ToString
    @Loggable(Loggable.DEBUG)
    @EqualsAndHashCode(of = "pull")
    final class Tool {
        /**
         * Encapsulated pull request.
         */
        private final transient Pull pull;
        /**
         * Public ctor.
         * @param pll Pull request
         */
        public Tool(final Pull pll) {
            this.pull = pll;
        }
        /**
         * Is it open?
         * @return TRUE if it's open
         * @throws IOException If fails
         */
        public boolean isOpen() throws IOException {
            // @checkstyle MultipleStringLiterals (1 line)
            return "open".equals(this.state());
        }
        /**
         * Get its state.
         * @return State of pull request
         * @throws IOException If fails
         */
        public String state() throws IOException {
            // @checkstyle MultipleStringLiterals (1 line)
            final String state = this.pull.json().getString("state");
            if (state == null) {
                throw new IllegalStateException(
                    String.format(
                        "state is NULL is pull request #%d", this.pull.number()
                    )
                );
            }
            return state;
        }
        /**
         * Change its state.
         * @param state State of pull request
         * @throws IOException If fails
         */
        public void state(final String state) throws IOException {
            this.pull.patch(
                Json.createObjectBuilder().add("state", state).build()
            );
        }
        /**
         * Get its body.
         * @return Body of pull request
         * @throws IOException If fails
         */
        public String title() throws IOException {
            // @checkstyle MultipleStringLiterals (1 line)
            final String title = this.pull.json().getString("title");
            if (title == null) {
                throw new IllegalStateException(
                    String.format(
                        "title is NULL is issue #%d", this.pull.number()
                    )
                );
            }
            return title;
        }
        /**
         * Change its state.
         * @param text Text of pull request
         * @throws IOException If fails
         */
        public void title(final String text) throws IOException {
            this.pull.patch(
                Json.createObjectBuilder().add("title", text).build()
            );
        }
        /**
         * Get its title.
         * @return Title of pull request
         * @throws IOException If fails
         */
        public String body() throws IOException {
            // @checkstyle MultipleStringLiterals (1 line)
            final String body = this.pull.json().getString("body");
            if (body == null) {
                throw new IllegalStateException(
                    String.format(
                        "body is NULL is issue #%d", this.pull.number()
                    )
                );
            }
            return body;
        }
        /**
         * Change its body.
         * @param text Body of pull request
         * @throws IOException If fails
         */
        public void body(final String text) throws IOException {
            this.pull.patch(
                Json.createObjectBuilder().add("body", text).build()
            );
        }
        /**
         * Get its URL.
         * @return URL of pull request
         * @throws IOException If fails
         */
        public URL url() throws IOException {
            final String url = this.pull.json().getString("url");
            if (url == null) {
                throw new IllegalStateException(
                    String.format(
                        "url is NULL is issue #%d", this.pull.number()
                    )
                );
            }
            try {
                return new URL(url);
            } catch (MalformedURLException ex) {
                throw new IllegalStateException(ex);
            }
        }
        /**
         * Get its HTML URL.
         * @return URL of pull request
         * @throws IOException If fails
         */
        public URL htmlUrl() throws IOException {
            final String url = this.pull.json().getString("html_url");
            if (url == null) {
                throw new IllegalStateException(
                    String.format(
                        "html_url is NULL is issue #%d", this.pull.number()
                    )
                );
            }
            try {
                return new URL(url);
            } catch (MalformedURLException ex) {
                throw new IllegalStateException(ex);
            }
        }
        /**
         * When this pull request was created.
         * @return Date of creation
         * @throws IOException If fails
         */
        public Date createdAt() throws IOException {
            final String date = this.pull.json().getString("created_at");
            if (date == null) {
                throw new IllegalStateException(
                    String.format(
                        "created_at is NULL is issue #%d", this.pull.number()
                    )
                );
            }
            return new Time(date).date();
        }
        /**
         * When this pull request was updated.
         * @return Date of update
         * @throws IOException If fails
         */
        public Date updatedAt() throws IOException {
            final String date = this.pull.json().getString("updated_at");
            if (date == null) {
                throw new IllegalStateException(
                    String.format(
                        "updated_at is NULL is issue #%d", this.pull.number()
                    )
                );
            }
            return new Time(date).date();
        }
        /**
         * When this pull request was closed.
         * @return Date of closing
         * @throws IOException If fails
         */
        public Date closedAt() throws IOException {
            final String date = this.pull.json().getString("closed_at");
            if (date == null) {
                throw new IllegalStateException(
                    String.format(
                        "closed_at is NULL is issue #%d", this.pull.number()
                    )
                );
            }
            return new Time(date).date();
        }
        /**
         * When this pull request was merged.
         * @return Date of merging
         * @throws IOException If fails
         */
        public Date mergedAt() throws IOException {
            final String date = this.pull.json().getString("merged_at");
            if (date == null) {
                throw new IllegalStateException(
                    String.format(
                        "merged_at is NULL is issue #%d", this.pull.number()
                    )
                );
            }
            return new Time(date).date();
        }
    }

}
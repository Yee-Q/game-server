package com.styeeqan.server.common.exception;

import com.styeeqan.server.common.error.GameServerError;
import org.slf4j.helpers.MessageFormatter;

/**
 * @author yeeq
 * @date 2022/5/29
 */
public class GameServerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final GameServerError error;

    private GameServerException(GameServerError error, String message, Throwable exp) {
        super(message, exp);
        this.error = error;
    }

    private GameServerException(GameServerError error, String message) {
        super(message);
        this.error = error;
    }

    public GameServerError getError() {
        return error;
    }

    public static Builder newBuilder(GameServerError error) {
        return new Builder(error);
    }

    public static class Builder {

        private final GameServerError error;
        private String message;
        private Throwable exp;

        public Builder(GameServerError error) {
            this.error = error;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder message(String format, Object... args) {
            this.message = MessageFormatter.arrayFormat(format, args).getMessage();
            return this;
        }

        public Builder causeBy(Throwable exp) {
            this.exp = exp;
            return this;
        }

        public GameServerException build() {

            String msg = this.error.toString();
            StringBuilder str = new StringBuilder(msg);

            if (this.message != null) {
                str.append("   ").append(this.message);
            }
            if (this.exp == null) {
                return new GameServerException(this.error, str.toString());
            } else {
                return new GameServerException(this.error, str.toString(), this.exp);
            }
        }
    }
}

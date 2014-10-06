package uk.gov.homeoffice.swappr.web.logging;

import com.equalexperts.logging.LogMessage;

public enum SwapprLogger implements LogMessage {
    UnknownError("SWP-00001", "Unknown error"), ConfigurationError("SWP-00002", "Configuration error: usage \n%s");

    private String code;
    private String messagePattern;

    SwapprLogger(String code, String pattern) {
        this.code = code;
        this.messagePattern = pattern;
    }

    @Override
    public String getMessageCode() {
        return code;
    }

    @Override
    public String getMessagePattern() {
        return messagePattern;
    }
}

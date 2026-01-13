package rmit.saintgiong.mediaapi.internal.common.type;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class KafkaTopic {
    public static final String COMPANY_REGISTRATION_REQUEST_TOPIC = "JM_COMPANY_REGISTRATION";
    public static final String COMPANY_REGISTRATION_REPLY_TOPIC = "JM_COMPANY_REGISTRATION_REPLIED";

    public static final String JM_UPDATE_LOGO_REQUEST_TOPIC = "JM_UPDATE_LOGO_REQUEST";
    public static final String JM_UPDATE_LOGO_RESPONSE_TOPIC = "JM_UPDATE_LOGO_RESPONSE";
}

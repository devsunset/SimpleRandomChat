/*
 * @(#)GenderOption.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat.modules.etcservice;

/**
 * <PRE>
 * SimpleRandomChat GenderOption
 * </PRE>
 *
 * @author devsunset
 * @version 1.0
 * @since SimpleRandomChat 1.0
 */
public class GenderOption {

    public final String gender_description;
    public final String gender_value;

    public GenderOption(String gender_value, String gender_description) {
        this.gender_description = gender_description;
        this.gender_value = gender_value;
    }

    @Override
    public String toString() {
        return "GenderOption{" +
                "gender_description='" + gender_description + '\'' +
                ", gender_value='" + gender_value + '\'' +
                '}';
    }
}

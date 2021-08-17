package hu.schdesign.solarboat;

import hu.schdesign.solarboat.model.SponsorGroup;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

@Convert
public class SponsorEnumConverter implements AttributeConverter<SponsorGroup, Integer> {
    @Override
    public Integer convertToDatabaseColumn(SponsorGroup sg){
        if(sg == null)
            return null;
        switch (sg) {
            case MAIN:
                return 0;

            case TOP:
                return 1;

            case OTHER:
                return 2;

            case PARTNER:
                return 3;

            case UNI:
                return 4;

            default:
                throw new IllegalArgumentException(sg + " not supported.");
        }
    }

    @Override
    public SponsorGroup convertToEntityAttribute(Integer string) {

            if (string == null)
                return null;

            switch (string) {
                case 0:
                    return SponsorGroup.MAIN;

                case 1:
                    return SponsorGroup.TOP;

                case 2:
                    return SponsorGroup.OTHER;

                case 3:
                    return SponsorGroup.PARTNER;

                case 4:
                    return SponsorGroup.UNI;

                default:
                    throw new IllegalArgumentException(string + " not supported.");
            }
    }
}

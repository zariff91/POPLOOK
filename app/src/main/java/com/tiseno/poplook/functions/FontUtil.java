package com.tiseno.poplook.functions;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by billy on 6/3/15.
 */
public class FontUtil {

    private static Typeface AvenirBlackFont;
    private static Typeface AvenirRomanFont;
    private static Typeface AvenirMediumFont;

    public static enum FontType {

        AVENIR_BLACK_FONT {
            public String toString() {
                return "Avenir-Black.otf";
            }
        },
        AVENIR_ROMAN_FONT {
            public String toString() {
                return "Avenir-Roman.otf";
            }
        },
        AVENIR_MEDIUM_FONT {
            public String toString() {
                return "Avenir-Medium.otf";
            }
        },
    }

    /**
     * @return Typeface Instance with the font passed as parameter
     */
    public static Typeface getTypeface(Context context, String typefaceName) {
        Typeface typeFace = null;
        //Check the name of the typeface and set the typeface
        try {

            if (typefaceName.equals(FontType.AVENIR_BLACK_FONT.toString())) {

                if (AvenirBlackFont == null) {
                    AvenirBlackFont = Typeface.createFromAsset(
                            context.getAssets(), typefaceName);
                }

                typeFace = AvenirBlackFont;

            }
            else if (typefaceName.equals(FontType.AVENIR_ROMAN_FONT.toString())) {

                if (AvenirRomanFont == null) {
                    AvenirRomanFont = Typeface.createFromAsset(
                            context.getAssets(), typefaceName);
                }

                typeFace = AvenirRomanFont;

            }
            if (typefaceName.equals(FontType.AVENIR_MEDIUM_FONT.toString())) {

                if (AvenirMediumFont == null) {
                    AvenirMediumFont = Typeface.createFromAsset(
                            context.getAssets(), typefaceName);
                }

                typeFace = AvenirMediumFont;

            }
        } catch (Exception ex) {
            typeFace = Typeface.DEFAULT;
        }

        return typeFace;
    }

    /**
     * @return Typeface Instance with the font passed as parameter
     */
    public static Typeface getTypeface(Context context, FontType typefaceName) {
        return getTypeface(context, typefaceName.toString());
    }
}

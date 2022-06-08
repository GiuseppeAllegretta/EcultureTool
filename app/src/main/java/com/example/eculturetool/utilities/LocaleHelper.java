package com.example.eculturetool.utilities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Locale;


/**
 * Classe che si occupa della gestione del multilingua
 */
public class LocaleHelper {
    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";
    public static Context onAttach(Context context) {
        String lang = getPersistedData(context, Locale.getDefault().getLanguage());
        return setLocale(context, lang);
    }
    public static Context onAttach(Context context, String defaultLanguage) {
        String lang = getPersistedData(context, defaultLanguage);
        return setLocale(context, lang);
    }

    /**
     * Metoto che consente di ottenere la lingua attualmente settata dall'applicazione
     * @param context il contesto che deve essere passato come parametro
     * @return il nome della lingua che è attualmente impostata
     */
    public static String getLanguage(Context context) {
        return getPersistedData(context, Locale.getDefault().getLanguage());
    }

    /**
     * Metoto che consente di settare una lingua
     * @param context il contesto che deve essere passato come parametro
     * @param language da impostare
     * @return il contesto
     */
    public static Context setLocale(Context context, String language) {
        persist(context, language);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResourcesLegacy(context, language);
        }
        return updateResourcesLegacy(context, language);
    }

    /**
     * Metodo che si occupa di gestire la persistenza della lingua
     * @param context il contesto che deve essere passato come parametro
     * @param defaultLanguage lingua di default dell'applicazione
     * @return la lingua impostata
     */
    private static String getPersistedData(Context context, String defaultLanguage) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SELECTED_LANGUAGE, defaultLanguage);
    }

    /**
     * Metoto che consente di rendere persistente la lingua che si vuole impostare
     * @param context il contesto che deve essere passato come parametro
     * @param language da rendere persistente
     */
    private static void persist(Context context, String language) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SELECTED_LANGUAGE, language);
        editor.apply();
    }
    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        return context.createConfigurationContext(configuration);
    }

    /**
     *
     * @param context il contesto che deve essere passato come parametro
     * @param language lingua da inserire
     * @return context aggiornato
     */
    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale);
        }
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }
}
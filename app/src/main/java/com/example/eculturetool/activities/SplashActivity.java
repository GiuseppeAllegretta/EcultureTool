package com.example.eculturetool.activities;

import static com.example.eculturetool.activities.oggetti.AggiungiOggettoActivity.PLACEHOLDER_OGGETTO;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eculturetool.R;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.database.SessionManagement;
import com.example.eculturetool.entities.Curatore;
import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.entities.Oggetto;
import com.example.eculturetool.entities.Tipologia;
import com.example.eculturetool.entities.TipologiaOggetto;
import com.example.eculturetool.entities.Zona;
import com.example.eculturetool.utilities.Permissions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG_LOG = SplashActivity.class.getName();
    private static final long MIN_WAIT_INTERVAL = 1500L;
    private static final long MAX_WAIT_INTERVAL = 2000L;
    private static final int GO_AHEAD_WHAT = 1;
    private long mStartTime = -1L;
    private boolean mIsDone;
    private DataBaseHelper dataBaseHelper;
    final String emailOspite = "admin@gmail.com";

    private static class UiHandler extends Handler {
        private final WeakReference<SplashActivity> mActivityRef;

        public UiHandler(final SplashActivity srcActivity) {
            this.mActivityRef = new WeakReference<>
                    (srcActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            final SplashActivity srcActivity = this.mActivityRef.get();
            if (srcActivity == null) {
                Log.d(TAG_LOG, "Reference to NoLeakSplashActivity lost!");
                return;
            }

            long elapsedTime = SystemClock.uptimeMillis() -
                    srcActivity.mStartTime;
            if (elapsedTime >= MIN_WAIT_INTERVAL && !srcActivity.mIsDone) {
                srcActivity.mIsDone = true;
                if (srcActivity.checkSession()) {
                    srcActivity.nextSessionActivity();

                } else {
                    srcActivity.nextActivity();
                }

            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        dataBaseHelper = new DataBaseHelper(getApplicationContext());
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);

        //Inizializza Handler
        UiHandler mHandler = new UiHandler(this);

        if (mStartTime == -1L) {
            mStartTime = SystemClock.uptimeMillis();
        }
        final Message goAheadMessage = mHandler.obtainMessage(GO_AHEAD_WHAT);
        mHandler.sendMessageAtTime(goAheadMessage, mStartTime + MAX_WAIT_INTERVAL);
    }

    private void nextActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void nextSessionActivity() {
        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        finish();
    }

    private boolean checkSession() {
        boolean flag = false;
        SessionManagement sessionManagement = new SessionManagement(SplashActivity.this);
        String emailCuratore = sessionManagement.getSession();
        if (emailCuratore.compareTo("-1") != 0) {
            if (emailCuratore.compareTo(emailOspite) == 0) {
                sessionManagement.removeSession();
                dataBaseHelper.setEmailCuratore(emailOspite);
                flag = false;
            } else {
                flag = true;
                dataBaseHelper.setEmailCuratore(sessionManagement.getSession());
            }
        }

        return flag;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!dataBaseHelper.checkEmailExist("balducci@gmail.com"))
            popolaDBmodalitaBalducci();
    }


    /**
     * Metodo che popola i dati del DB per fornire un account al professore
     */
    public void popolaDBmodalitaBalducci() {

        Permissions permissions = new Permissions();
        //Stringhe relative alle zone del museo
        final String duecento_e_trecento = "Duecento e Trecento";
        final String primo_rinascimento = "Primo Rinascimento";
        final String Il_rinascimento_oltre_firenze = "Il rinascimento oltre Firenze";
        final String secondo_rinascimento = "Secondo Rinascimento";
        final String cinquecento = "Cinquecento";

        final String emailOspite = "balducci@gmail.com";


        if(!dataBaseHelper.checkEmailExist(emailOspite)){
            //Creazione Curatore
            Curatore curatore = new Curatore("Fabrizio", "Balducci", emailOspite, "123456", null, -1);
            dataBaseHelper.addCuratore(curatore);

            //effettuo login
            //dataBaseHelper.login("admin", "123456");
            dataBaseHelper.setEmailCuratore(emailOspite);


            //Creazione Luogo
            Luogo uffizi = new Luogo("Galleria degli Uffizi", "La Galleria degli Uffizi è un museo statale di Firenze, che fa parte del complesso museale denominato Gallerie degli Uffizi e comprendente, oltre alla suddetta galleria, il Corridoio vasariano, le collezioni di Palazzo Pitti e il Giardino di Boboli, che insieme costituiscono per quantità e qualità delle opere raccolte uno dei più importanti musei del mondo.", Tipologia.MUSEO, emailOspite);
            dataBaseHelper.addLuogo(uffizi);

            //valore fittizio assegnato
            int idLuogo = 0;

            List<Luogo> luoghi = dataBaseHelper.getLuoghi();
            for(Luogo luogo: luoghi){
                if(luogo.getEmailCuratore().compareTo(emailOspite) == 0){
                    idLuogo = luogo.getId();
                    dataBaseHelper.setLuogoCorrente(idLuogo);
                }
            }


            //Creazione Zone
            List<Zona> zone = new ArrayList<>();
            zone.add(new Zona(duecento_e_trecento, "La sala è dedicata ai pittori toscani appartenenti alla fine del tredicesimo - inizio quattordicesimo secolo ed espone i dipinti più antichi della Galleria degli Uffizi.", idLuogo));
            zone.add(new Zona(primo_rinascimento, "In questa sala erano esposti alcuni capolavori del primo Rinascimento. La sala è stata restaurata e riaperta nella primavera 2015 ed adesso è dedicata a Gentile da Fabriano.", idLuogo));
            zone.add(new Zona(Il_rinascimento_oltre_firenze, "In questa sala sono esposti tutti i capolavori che fanno parte del rinascimento ma che non sono nativi di Firenze.", idLuogo));
            zone.add(new Zona(secondo_rinascimento, "Il secondo Rinascimento o Rinascimento Maturo si sviluppa dal 1500 al 1600 circa, principalmente a Roma dove i papi chiamano i migliori artisti del tempo per commissionare la realizzazione di grandi opere\n" +
                    "architettoniche e pittoriche, ma anche a Firenze, Venezia, e Milano Questa fase artistica ha prodotto opere meravigliose come edifici religiosi e civili, palazzi, ville, sculture, pitture su muro e su tavola, cicli di affreschi.", idLuogo));
            zone.add(new Zona(cinquecento, "Il secondo Rinascimento o Rinascimento Maturo si sviluppa dal 1500 al 1600 circa, principalmente a Roma dove i papi chiamano i migliori artisti del tempo per commissionare la realizzazione di grandi opere\n" +
                    "architettoniche e pittoriche, ma anche a Firenze, Venezia, e Milano. Questa fase artistica ha prodotto opere meravigliose come edifici religiosi e civili, palazzi, ville, sculture, pitture su muro e su tavola, cicli di affreschi.", idLuogo));

            for(Zona zona: zone){
                dataBaseHelper.aggiungiZona(zona);
            }

            //creazione oggetti
            zone = dataBaseHelper.getZone();

            for(Zona zona: zone){

                switch (zona.getNome()){
                    case duecento_e_trecento:

                        dataBaseHelper.addOggetto(new Oggetto("Maestà di Ognisanti", "La Madonna di Ognissanti è un dipinto a tempera e oro su tavola (335x229,5) di Giotto, databile al 1310 circa e conservato agli Uffizi di Firenze, dove è scenograficamente collocato a poca distanza da analoghe pale di Cimabue (Maestà di Santa Trinita) e Duccio di Buoninsegna (Madonna Rucellai).", "https://firebasestorage.googleapis.com/v0/b/auth-96a19.appspot.com/o/uploads%2Fobjects_images%2FGiottoMadonna.jpeg?alt=media&token=c67819ce-31fe-49aa-80b1-21b144889985", TipologiaOggetto.QUADRO, zona.getId()));
                        dataBaseHelper.addOggetto(new Oggetto("Fronte di sarcofago con fatiche d’Ercole", "Nel millenario racconto della mitologia greca, costellato di eroi e di imprese sovrumane, le dodici fatiche di Ercole occupano un ruolo centrale: le gesta, al termine delle quali l’eroe è assunto nell’Olimpo divenendo immortale, sono esempio di virtù e segno della gloria che è riconosciuta a chi affronta con dedizione prove e sacrifici.", "https://firebasestorage.googleapis.com/v0/b/auth-96a19.appspot.com/o/uploads%2Fobjects_images%2F5-2-.jpeg?alt=media&token=de810ed5-43a2-46f9-a817-02cb2373b8f7", TipologiaOggetto.SCULTURA, zona.getId()));
                        dataBaseHelper.addOggetto(new Oggetto("Vaso monumentale", "Il modello originario di questo monumentale vaso è esposto al Musée national des châteaux de Versailles et de Trianon e fu ideato dall’allievo dello scultore Michel-Ange Slodtz, Louis-Simon Boizot. Costui si formò all’Accademia di Francia a Roma dal 1765 al 1769, per giungere nel 1773 alla Manifattura di Sèvres, dove mantenne la carica di capo modellatore fino al 1780, proseguendo comunque la sua attività fino alla sua morte avvenuta nel 1809.", "https://firebasestorage.googleapis.com/v0/b/auth-96a19.appspot.com/o/uploads%2Fobjects_images%2FPRINC-Vaso-monumentale-Sevres.jpeg?alt=media&token=445b6c3d-f776-4723-9114-c17ec79dd3e4", TipologiaOggetto.ALTRO, zona.getId()));
                        dataBaseHelper.addOggetto(new Oggetto("Adorazione dei Magi", "Al termine di un lungo viaggio, i Magi giungono al cospetto del Messia appena venuto al mondo guidati dalla stella cometa, e si prostrano ai piedi della Sacra Famiglia offrendo doni preziosi. Abbigliati con vesti straordinariamente ricche e alla moda, i Magi sono accompagnati da un corteo multietnico, di cui fanno parte anche animali esotici, che sottintendono la loro provenienza dal lontano Oriente.", "https://firebasestorage.googleapis.com/v0/b/auth-96a19.appspot.com/o/uploads%2Fobjects_images%2F1506339984606859-gentile-adorazione-principale.jpeg?alt=media&token=c7ca2b29-2cdc-494d-bc3a-9f4e05abebc6", TipologiaOggetto.QUADRO, zona.getId()));

                        break;

                    case primo_rinascimento:
                        dataBaseHelper.addOggetto(new Oggetto("Sant'Anna, la Madonna col Bambino e cinque angeli (\"Sant'Anna Metterza\")", "La pala era destinata alla chiesa fiorentina di Sant’Ambrogio \"nella cappella che è allato alla porta che va al parlatorio delle monache\", come specificato da Vasari. Da recenti ricerche d'archivio si sa che l'altare era intitolato a Sant'Anna e che l'opera fu commissionata da Nofri Del Brutto Buonamici, esponente di una famiglia di tessitori assai devota alla Vergine e i cui stemmi un tempo erano visibili nella parte inferiore della tavola.", "https://firebasestorage.googleapis.com/v0/b/auth-96a19.appspot.com/o/uploads%2Fobjects_images%2F1557133506716136-08-masolino.jpeg?alt=media&token=68637b48-aaef-4758-b082-c67155520925", TipologiaOggetto.QUADRO, zona.getId()));
                        dataBaseHelper.addOggetto(new Oggetto("I duchi di Urbino Federico da Montefeltro e Battista Sforza", "Fra i più celebri ritratti del Rinascimento italiano, il dittico raffigura i signori di Urbino, Federico da Montefeltro (1422-1482) e sua moglie Battista Sforza (1446-1472). In accordo con la tradizione quattrocentesca, ispirata alla numismatica antica, le due figure sono rappresentate di profilo, taglio che garantiva una notevole verosimiglianza e precisione nella resa dei particolari, senza che trasparissero gli stati d’animo: i duchi di Urbino appaiono infatti immuni da turbamenti e emozioni. I coniugi sono affrontati e l’unità spaziale è suggerita dalla luce e dalla continuità del paesaggio collinare sullo sfondo – il paesaggio marchigiano su cui i Montefeltro regnavano. ", "https://firebasestorage.googleapis.com/v0/b/auth-96a19.appspot.com/o/uploads%2Fobjects_images%2F1506161515054059-556161.jpeg?alt=media&token=f85c9201-5812-48fe-ad9b-90d434a5bb9d", TipologiaOggetto.QUADRO, zona.getId()));
                        dataBaseHelper.addOggetto(new Oggetto("La Primavera", "Conosciuto con il nome convenzionale di Primavera, la pittura mostra nove figure della mitologia classica che incedono su un prato fiorito, davanti a un bosco di aranci e alloro. In primo piano a destra, Zefiro abbraccia e feconda la ninfa Clori, raffigurata poco oltre nelle sembianze di Flora, dea della fioritura.", "https://firebasestorage.googleapis.com/v0/b/auth-96a19.appspot.com/o/uploads%2Fobjects_images%2F1543409158632122-Botticelli-primavera-intero.jpeg?alt=media&token=1720d4a2-417c-4205-a097-46e409ff7874", TipologiaOggetto.QUADRO, zona.getId()));
                        break;

                    case Il_rinascimento_oltre_firenze:
                        dataBaseHelper.addOggetto(new Oggetto("Madonna in adorazione di Gesù Bambino", "L’origine di questo dipinto non ci è nota, ma è certo che doveva essere considerato uno dei capolavori del pittore se il duca di Mantova, Ferdinando Gonzaga, lo scelse per farne dono al granduca Cosimo II de’ Medici. L’opera giunse agli Uffizi nel 1617 e fu collocata in Tribuna dove rimase fino al 1848. Nella celebre veduta della Tribuna degli Uffizi del 1772 del pittore inglese Joahnn Zoffany l’opera occupa un posto di tutto riguardo accanto alla Madonna della seggiola di Raffaello.", "https://firebasestorage.googleapis.com/v0/b/auth-96a19.appspot.com/o/uploads%2Fobjects_images%2F1588092525509375-2.jpeg?alt=media&token=ebbbec73-be4e-4210-941d-c66a4307a335", TipologiaOggetto.QUADRO, zona.getId()));
                        dataBaseHelper.addOggetto(new Oggetto("Santo monaco e donatore", "E’ incerta l’identità del santo monaco che indossa, sopra la tonaca e lo scapolare di colore bianco, un prezioso piviale e impugna il bastone pastorale, entrambi prerogativa degli abati e dei vescovi. In base al colore della veste, potrebbe trattarsi del fondatore dell’ordine cistercense Bernardo di Chiaravalle (1090- 1153), oppure di Baudolino d’Alessandria (VIII secolo), santo molto venerato dall’ordine degli Umiliati e per questo a volte raffigurato con l’abito dell’ordine, sebbene non ne abbia mai fatto parte. Accanto a lui è inginocchiato un religioso dello stesso ordine, che tiene fra le mani il berretto in segno di ossequio", "https://firebasestorage.googleapis.com/v0/b/auth-96a19.appspot.com/o/uploads%2Fobjects_images%2F20-ok.jpeg?alt=media&token=d6d6a4d8-6169-4f98-a29e-dbc3079b2831", TipologiaOggetto.QUADRO, zona.getId()));
                        break;

                    case secondo_rinascimento:
                        dataBaseHelper.addOggetto(new Oggetto("Dioniso e Ampelo", "A causa delle numerose integrazioni cinquecentesche, questo pezzo può essere considerato alla stregua di un’opera moderna: solo il torso, acefalo e privo di braccia, che si conserva fino all’altezza delle ginocchia, è infatti un reperto originale di produzione antica, identificato oggi come una copia del più famoso “Narciso di Pompei”.", "https://firebasestorage.googleapis.com/v0/b/auth-96a19.appspot.com/o/uploads%2Fobjects_images%2FDioniso-Ampelo-prev.jpeg?alt=media&token=90e251dd-0536-4b1e-a040-cdaad3219b54", TipologiaOggetto.STATUA, zona.getId()));
                        dataBaseHelper.addOggetto(new Oggetto("Annunciazione", "Davanti ad un palazzo rinascimentale, in un rigoglioso giardino recintato che evoca l’hortus conclusus allusivo alla purezza di Maria, l’Arcangelo Gabriele si inginocchia davanti alla Vergine rivolgendole il saluto ed offrendole un giglio. ", "https://firebasestorage.googleapis.com/v0/b/auth-96a19.appspot.com/o/uploads%2Fobjects_images%2F1503990074029518-568314.jpeg?alt=media&token=459bed81-8a8e-4268-bc61-510a692be607", TipologiaOggetto.QUADRO, zona.getId()));
                        dataBaseHelper.addOggetto(new Oggetto("San Giovannino", "Il San Giovannino di Raffaello segue un modello iconografico creatosi nelle fonti agiografiche medievali e delle ‘Vite del Battista’ o ‘Storie di San Giovanni Battista’ diffuse tra XV e XVI secolo. In quegli scritti si allude alle precoci esperienze di vita eremitica del piccolo Giovanni, che fin dai cinque anni di età era solito perdersi nella solitudine di boschi deserti, ritirato in un antro e appena coperto da una pelle di leopardo.", "https://firebasestorage.googleapis.com/v0/b/auth-96a19.appspot.com/o/uploads%2Fobjects_images%2F21.san-giovanni-battista-raffaello.jpg?alt=media&token=fbb1b118-cd46-47be-839c-416329360354", TipologiaOggetto.QUADRO, zona.getId()));

                        break;

                    case cinquecento:
                        dataBaseHelper.addOggetto(new Oggetto("Venere di Urbino", "L’ opera è una delle più celebri di Tiziano e ritrae una figura emblematica di giovane sposa in procinto di essere abbigliata per prendere parte alla celebrazione del rito noto a Venezia come “il toccamano”. Si trattava di una cerimonia a carattere domestico e non ecclesiastico durante la quale le giovani donne richieste in matrimonio, toccando la mano dello sposo, esprimevano il loro consenso alle nozze.", "https://firebasestorage.googleapis.com/v0/b/auth-96a19.appspot.com/o/uploads%2Fobjects_images%2F1549024578521584-14.VenereHeader.jpg?alt=media&token=ee3fe0c4-dd03-4e9d-a9d0-e2506480ee11", TipologiaOggetto.QUADRO, zona.getId()));
                        break;
                }
            }

            if(permissions.checkConnection(getApplicationContext())){
                List<Oggetto> oggetti = dataBaseHelper.getOggetti();

                for (Oggetto oggetto: oggetti){
                    if(oggetto.getId() != -1 ) {
                        Bitmap bitmap = qrCodeGenerator(oggetto.getId());
                        uploadFile(oggetto.getId(), bitmap);
                    }
                }
            }else {
                List<Oggetto> oggetti = dataBaseHelper.getOggetti();

                for(Oggetto oggetto: oggetti){
                    dataBaseHelper.setImageOggetto(oggetto.getId(), PLACEHOLDER_OGGETTO);
                }
            }
        }
    }



    /**
     * Metodo che si occupa di generare il QRcode dell'oggetto appena creato
     * @param idOggetto identificativo dell'oggetto appena creato
     * @return bitmap: ovvero l'oggetto di tipo Bitmap che rappresenta il QRcode
     */
    private Bitmap qrCodeGenerator(int idOggetto) {
        Bitmap bitmap = null;

        //inizializzazione multi format writer
        MultiFormatWriter writer = new MultiFormatWriter();

        try {
            //inizializzazione bit matrix
            BitMatrix matrix = writer.encode(String.valueOf(idOggetto), BarcodeFormat.QR_CODE, 350, 350);

            //inizializzazione barcode encoder
            BarcodeEncoder encoder = new BarcodeEncoder();

            //inizializzazione bitmap
            bitmap = encoder.createBitmap(matrix);

        } catch (WriterException e) {
            e.printStackTrace();
        }

        return bitmap;
    }



    /**
     * Metodo che consente di caricare su Firestore un oggetto di tipo bitmap in formato jpeg
     *
     */
    public void uploadFile(int idOggetto, Bitmap bitmap) {
        String STORREF = "gs://auth-96a19.appspot.com/";
        StorageReference mStorageReference = FirebaseStorage.getInstance(STORREF).getReference("uploads/qrCode");
        StorageReference fileReferences = mStorageReference.child(System.currentTimeMillis() + "." + "jpeg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        //Ottiene l'uri e lo salva su SQLite
        //Salva su SQLite la stringa dell'url
        StorageTask uploadTask = fileReferences.putBytes(data)
                .addOnFailureListener(exception -> {
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                }).addOnSuccessListener(taskSnapshot -> {
                    //Ottiene l'uri e lo salva su SQLite
                    fileReferences.getDownloadUrl().addOnSuccessListener(uri -> {
                        //Salva su SQLite la stringa dell'url
                        dataBaseHelper.setQRCode(idOggetto, uri.toString());
                        finish();
                    });
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    }
                });
    }
}
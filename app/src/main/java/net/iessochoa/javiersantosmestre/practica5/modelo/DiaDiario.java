package net.iessochoa.javiersantosmestre.practica5.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DiaDiario implements Parcelable {

    private Date fecha;
    private int valoracionDia;
    private String resumen;
    private String contenido;
    private String fotoUri;
    private String latitud;
    private String longitud;

    public DiaDiario(Date fecha, int valoracionDia, String resumen, String contenido, String fotoUri) {
        this.fecha = fecha;
        this.valoracionDia = valoracionDia;
        this.resumen = resumen;
        this.contenido = contenido;
        this.fotoUri = fotoUri;
        this.latitud = "";
        this.longitud = "";
    }

    public DiaDiario(Date fecha, int valoracionDia, String resumen, String contenido) {
        this.fecha = fecha;
        this.valoracionDia = valoracionDia;
        this.resumen = resumen;
        this.contenido = contenido;
        this.latitud = "";
        this.longitud = "";
    }

    public DiaDiario() {

    }

    protected DiaDiario(Parcel in) {
        long tmpFecha = in.readLong();
        this.fecha = tmpFecha == -1 ? null : new Date(tmpFecha);
        valoracionDia = in.readInt();
        resumen = in.readString();
        contenido = in.readString();
        fotoUri = in.readString();
        latitud = in.readString();
        longitud = in.readString();
    }

    public static final Creator<DiaDiario> CREATOR = new Creator<DiaDiario>() {
        @Override
        public DiaDiario createFromParcel(Parcel in) {
            return new DiaDiario(in);
        }

        @Override
        public DiaDiario[] newArray(int size) {
            return new DiaDiario[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.fecha != null ? this.fecha.getTime() : -1);
        parcel.writeInt(valoracionDia);
        parcel.writeString(resumen);
        parcel.writeString(contenido);
        parcel.writeString(fotoUri);
        parcel.writeString(latitud);
        parcel.writeString(longitud);
    }

    //Getter y setters de las distintas variables de DiaDiario
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getValoracionDia() {
        return valoracionDia;
    }

    public void setValoracionDia(int valoracionDia) {
        this.valoracionDia = valoracionDia;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String getFotoUri() {
        return fotoUri;
    }

    public void setFotoUri(String fotoUri) {
        this.fotoUri = fotoUri;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }


    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    //Metodo para mostrar el icono en función de la valoración del dia
    public int getValoracionResumida(){
        if(this.getValoracionDia() < 5){
            return 1;
        }else if(this.getValoracionDia()<8){
            return 2;
        }
        return 3;
    }

    public String muestraDia(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(this.fecha) +" - " + this.getResumen()+ "\n Valoración: "+this.valoracionDia;
    }

    @Override
    public String toString() {
        return "DiaDiario{" +
                "fecha=" + fecha +
                ", valoracionDia=" + valoracionDia +
                ", resumen='" + resumen + '\'' +
                ", contenido='" + contenido + '\'' +
                ", fotoUri='" + fotoUri + '\'' +
                ", latitud='" + latitud + '\'' +
                ", longitud='" + longitud + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiaDiario diaDiario = (DiaDiario) o;
        return getFecha().equals(diaDiario.getFecha());
    }

}

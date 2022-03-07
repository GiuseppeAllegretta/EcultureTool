package com.example.eculturetool.entities;

import androidx.annotation.NonNull;


    public class Oggetto {
        private String nome, anno, tipologia;

        public String getNome() {
            return nome;
        }

        public String getAnno() {
            return anno;
        }

        public String getTipologia() {
            return tipologia;
        }

        public Oggetto(){

        }

        public Oggetto(String n, String a, String t){
            this.nome=n;
            this.anno=a;
            this.tipologia=t;
        }
    }


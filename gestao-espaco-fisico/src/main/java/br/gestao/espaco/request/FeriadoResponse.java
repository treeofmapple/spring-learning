package br.gestao.espaco.request;

import java.time.LocalDate;

public record FeriadoResponse(Long Id, String Nome, LocalDate Data) {

}

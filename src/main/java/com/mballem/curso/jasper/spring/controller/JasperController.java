package com.mballem.curso.jasper.spring.controller;

import com.mballem.curso.jasper.spring.repository.EnderecoRepository;
import com.mballem.curso.jasper.spring.repository.FuncionarioRepository;
import com.mballem.curso.jasper.spring.repository.NivelRepository;
import com.mballem.curso.jasper.spring.service.JasperService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@Controller
public class JasperController {

    @Autowired
    private JasperService service;
    @Autowired
    private NivelRepository nivelRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private FuncionarioRepository funcionarioRepository;


    @GetMapping("/reports")
    public String abrir(){
        return "reports";
    }

    @GetMapping("/relatorio/pdf/jr1")
    public void exibirRelatorio01(@RequestParam ("code") String code,
                                  @RequestParam ("acao") String acao,
                                  HttpServletResponse response) throws IOException {
        byte[] bytes = service.exportarPDF(code);
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        if(acao.equals("v")){
            response.setHeader("Content-disposition", "inline; filename=relatorio-"+code+".pdf");
        } else {
            response.setHeader("Content-disposition", "attachment; filename=relatorio-"+code+".pdf");
        }

        response.getOutputStream().write(bytes);
    }

    @GetMapping("/relatorio/pdf/jr9/{code}")
    public void exibirRelatorio09(@PathVariable("code") String code,
                                  @RequestParam (name = "nivel", required = false) String nivel,
                                  @RequestParam (name = "uf", required = false) String uf,
                                  HttpServletResponse response) throws IOException {
        service.addParams("NIVEL_DESC",nivel.isEmpty() ? null : nivel);
        service.addParams("UF",uf.isEmpty() ? null : uf);
        byte[] bytes = service.exportarPDF(code);
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);

        response.setHeader("Content-disposition", "inline; filename=relatorio-"+code+".pdf");

        response.getOutputStream().write(bytes);
    }


    @GetMapping("/relatorio/pdf/jr19/{code}")
    public void exibirRelatorio19(@PathVariable("code") String code,
                                  @RequestParam (name = "idf", required = false) Long id,
                                  HttpServletResponse response) throws IOException {
        service.addParams("ID_FUNCIONARIO",id);
        byte[] bytes = service.exportarPDF(code);
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);

        response.setHeader("Content-disposition", "inline; filename=relatorio-"+code+".pdf");

        response.getOutputStream().write(bytes);
    }

    @GetMapping("/relatorio/html/jr19/{code}")
    public void exibirRelatorio19HTML(@PathVariable("code") String code,
                                  @RequestParam (name = "idf", required = false) Long id, HttpServletRequest request,
                                  HttpServletResponse response) throws JRException {


        response.setContentType(MediaType.TEXT_HTML_VALUE);
        service.addParams("ID_FUNCIONARIO",id);
        service.exportarHTML(code,request,response).exportReport();
    }

    @GetMapping("/buscar/funcionarios")
    public ModelAndView buscarFuncionariosPorNome(@RequestParam("nome") String nome){
        return new ModelAndView("reports","funcionarios",funcionarioRepository.findFuncionariosByNome(nome));
    }

    @ModelAttribute("niveis")
    public List<String> getNiveis(){
        return nivelRepository.findNiveis();
    }

    @ModelAttribute("ufs")
    public List<String> getUfs(){
        return enderecoRepository.findUfs();
    }



}

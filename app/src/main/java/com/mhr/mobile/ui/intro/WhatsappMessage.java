package com.mhr.mobile.ui.intro;

import com.google.gson.annotations.SerializedName;

public class WhatsappMessage {
  @SerializedName("messaging_product")
  private String messagingProduct = "whatsapp";

  @SerializedName("to")
  private String to;

  @SerializedName("type")
  private String type = "template";

  @SerializedName("template")
  private Template template;

  public WhatsappMessage(String to, String templateName, String otp, String duration) {
    this.to = to;
    this.template = new Template(templateName, otp, duration);
  }

  private static class Template {
    @SerializedName("name")
    private String name;

    @SerializedName("language")
    private Language language;

    @SerializedName("components")
    private Component[] components;

    public Template(String name, String otp, String duration) {
      this.name = name;
      this.language = new Language("id");
      this.components = new Component[] {new Component(otp, duration)};
    }
  }

  private static class Language {
    @SerializedName("code")
    private String code;

    public Language(String code) {
      this.code = code;
    }
  }

  private static class Component {
    @SerializedName("type")
    private String type = "body";

    @SerializedName("parameters")
    private Parameter[] parameters;

    public Component(String otp, String duration) {
      this.parameters =
          new Parameter[] {new Parameter("text", otp), new Parameter("text", duration)};
    }
  }

  private static class Parameter {
    @SerializedName("type")
    private String type;

    @SerializedName("text")
    private String text;

    public Parameter(String type, String text) {
      this.type = type;
      this.text = text;
    }
  }
}

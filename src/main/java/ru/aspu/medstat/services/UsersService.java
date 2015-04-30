package ru.aspu.medstat.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.aspu.medstat.entities.Statistic;
import ru.aspu.medstat.entities.User;
import ru.aspu.medstat.utils.EmailUtils;

@Service
public class UsersService {
    @Autowired
    private MailService mail;
    
    @Autowired
    private StatisticsService statService;

    private SimpleDateFormat birthDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat gymDateFormat = new SimpleDateFormat("yyyy/MM/dd");

    public String getErrors(User user) {
        String errors = "";
        if (!EmailUtils.validate(user.email)) {
            errors += "Невалидный адрес эл. почты. Принимается паттерн вида *@*\n";
        }
        if (user.firstName.length() == 0) {
            errors += "Не допускается пустое имя пользователя\n";
        }
        if (user.lastName.length() == 0) {
            errors += "Не допускается пустая фамилия пользователя\n";
        }
        if (user.password.length() < 6) {
            errors += "Допускается пароль длиной от 6 символов\n";
        }

        if (user.birthDate.length() == 0) {
            errors += "Не допускается отсутствие даты рождения\n";
        }

        try {
            birthDateFormat.parse(user.birthDate);
        } catch (ParseException e) {
            errors += "Неверный формат даты рождения. Ожидается dd/MM/yyyy\n";
        }

        return errors;
    }

    public void sendMail(User user) {
        mail.send(user.email, "Медицинский портал АГУ. Регистрация", String.format(
                "<a href=\"http://localhost:8080/auth/confirm/%s\">Нажмите сюда для окончания регистрации</a>",
                user.emailToken));
    }
    
    public JSONObject userToJson(User user) {
    	JSONObject json = new JSONObject();
    	
    	json.put("id", user.id);
    	json.put("email", user.email);
    	json.put("first_name", user.firstName);
    	json.put("last_name", user.lastName);
    	json.put("birth_date", user.birthDate);
    	json.put("role", user.role);
    	json.put("phone", user.phone);
    	json.put("doctorId", user.doctorId);
    	
    	JSONArray stats = new JSONArray();
    	
    	Map<Long, JSONArray> statMap = new HashMap<>();
    	for (Statistic stat : statService.getAllActualUserStats(user.id)) {
    		JSONObject jsonStat = new JSONObject();
    		jsonStat.put("stat_id", stat.id);
    		jsonStat.put("gym_id", stat.getUserGym().getGymnastic().id);
    		jsonStat.put("stat_date", gymDateFormat.format(stat.date));
    		jsonStat.put("percent", stat.percent);
    		jsonStat.put("gym_title", stat.getUserGym().getGymnastic().title);
    		
    		if (statMap.containsKey(stat.getUserGym().getGymnastic().id)) {
    			statMap.get(stat.getUserGym().getGymnastic().id).add(jsonStat);
    		} else {
    			JSONArray jsonStatList = new JSONArray();
    			jsonStatList.add(jsonStat);
    			statMap.put(stat.getUserGym().getGymnastic().id, jsonStatList);
    		}
    	}
    	
    	for (Map.Entry<Long, JSONArray> e : statMap.entrySet()) {
    		JSONObject jsonObj = new JSONObject();
    		jsonObj.put(e.getKey(), e.getValue());
    		stats.add(jsonObj);
    	}
    	
    	json.put("stats", stats);
    	
    	return json;
    }
}

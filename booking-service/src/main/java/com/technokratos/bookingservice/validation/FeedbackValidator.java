package com.technokratos.bookingservice.validation;

import org.springframework.stereotype.Component;
import com.technokratos.bookingservice.dto.forms.FeedbackForm;

@Component
public class FeedbackValidator {
    public Validation validate(FeedbackForm feedbackForm) {
        Validation validation = new Validation();
        if(feedbackForm.getUserId() == null ) {
            validation.addError("пользователь должен быть обозначен");
        }
        if(feedbackForm.getEventId() == null) {
            validation.addError("мероприятие должно быть обозначено");
        }
        if( feedbackForm.getScore() == null || feedbackForm.getScore()>5 || feedbackForm.getScore()<1) {
            validation.addError("оценка должна быть в пределах от 1 до 5");
        }
        if(feedbackForm.getText() == null || feedbackForm.getText().isEmpty()) {
            validation.addError("комментарий не может быть пустым");
        }
        return validation;
    }
}

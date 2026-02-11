package com.github.salilvnair.convengdemo.transformer;

import com.github.salilvnair.ccf.core.model.*;
import com.github.salilvnair.convengine.container.annotation.ContainerDataTransformer;
import com.github.salilvnair.convengine.container.transformer.ContainerDataTransformerHandler;
import com.github.salilvnair.convengine.engine.session.EngineSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
@ContainerDataTransformer(
        intent = "FAQ",
        state = "IDLE"
)
public class FaqContainerTransformer implements ContainerDataTransformerHandler {

    @Override
    public Map<String, Object> transform(ContainerComponentResponse response, EngineSession engineSession, Map<String, Object> inputParams) {

        Map<Integer, PageDataResponse> pages = response.getPages();
        List<FaqItem> faqs = new ArrayList<>();

        if (pages == null) {
            return null;
        }

        for (PageDataResponse page : pages.values()) {

            if (page.getSections() == null) continue;

            for (SectionData section : page.getSections().values()) {

                if (section.getData() == null) continue;

                for (ContainerData container : section.getData()) {

                    List<List<SectionField>> tableData =
                            container.getTableData();

                    if (tableData == null) continue;

                    for (List<SectionField> row : tableData) {

                        Long faqId = null;
                        String question = null;
                        String answer = null;

                        for (SectionField field : row) {

                            String name = field.getFieldDisplayName();
                            Object value = field.getFieldValue();

                            if (value == null || name == null) continue;

                            switch (name) {
                                case "FAQ ID" -> {
                                    if (value instanceof Number n) {
                                        faqId = n.longValue();
                                    }
                                }
                                case "Question" -> {
                                    question = value.toString();
                                }
                                case "Answer" -> {
                                    answer = value.toString();
                                }
                            }
                        }

                        if (faqId != null && question != null && answer != null) {
                            faqs.add(new FaqItem(faqId, question, answer));
                        }
                    }
                }
            }
        }

        // Replace container payload with DOMAIN JSON

        return Map.of(
                "faqs", faqs
        );
    }

    @Data
    @AllArgsConstructor
    public static class FaqItem {
        private Long faqId;
        private String question;
        private String answer;
    }
}

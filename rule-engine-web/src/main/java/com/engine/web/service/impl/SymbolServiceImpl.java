package com.engine.web.service.impl;

import com.engine.core.condition.Operator;
import com.engine.core.value.DataType;
import com.engine.web.service.SymbolService;
import com.engine.web.vo.symbol.SymbolResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/14
 * @since 1.0.0
 */
@Service
public class SymbolServiceImpl implements SymbolService {

    /**
     * 规则引擎运算符
     *
     * @param valueType 例如：CONTROLLER
     * @return >,<,=..
     */
    @Override
    public List<SymbolResponse> getByType(String valueType) {
        DataType dataType = DataType.getByValue(valueType);
        List<Operator> symbol = dataType.getSymbol();
        return symbol.stream().map(m -> {
            SymbolResponse symbolResponse = new SymbolResponse();
            symbolResponse.setName(m.name());
            symbolResponse.setExplanation(m.getExplanation());
            symbolResponse.setSymbol(m.getSymbol());
            return symbolResponse;
        }).collect(Collectors.toList());
    }
}

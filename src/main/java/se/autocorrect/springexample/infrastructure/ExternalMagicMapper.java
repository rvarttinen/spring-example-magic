/*-
 *  
 * spring-example-magic
 *  
 * Copyright (C) 2025 Autocorrect Design HB
 *  
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *  
 */
package se.autocorrect.springexample.infrastructure;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import se.autocorrect.springexample.model.MagicStuff;
import se.autocorrect.springexample.model.MagicStuffDto;

@Mapper
public interface ExternalMagicMapper {

	ExternalMagicMapper INSTANCE = Mappers.getMapper(ExternalMagicMapper.class);

    @Mapping(source = "key", target = "id")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "activity", target = "magicString")
    MagicStuff externalMagicToMagicStuff(ExternalMagic externalMagic);

    @Mapping(source = "id", target = "key")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "magicString", target = "activity")
    ExternalMagic magicStuffToExternalMagic(MagicStuff magicStuff);

    @Mapping(source = "identity", target = "key")
    @Mapping(source = "typeOfMagic", target = "type")
    @Mapping(source = "magicString", target = "activity")
    ExternalMagic magicStuffDtoToMagicStuff(MagicStuffDto magicStuffDto);

    List<MagicStuff> map(List<ExternalMagic> externalMagics);
}

/**
 * Copyright 2017- Mark C. Slee, Heron Arts LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Mark C. Slee <mark@heronarts.com>
 */

package heronarts.lx.osc;

import java.nio.ByteBuffer;

public class OscString implements OscArgument {

  private String value;
  private int byteLength;

  public OscString(char[] value) {
    this(new String(value));
  }

  public OscString(String value) {
    setValue(value);
  }

  public OscString setValue(String value) {
    this.value = value;
    this.byteLength = value.length() + 1;
    while (this.byteLength % 4 > 0) {
      ++this.byteLength;
    }
    return this;
  }

  public String getValue() {
    return this.value;
  }

  public int getByteLength() {
    return this.byteLength;
  }

  public static OscString parse(byte[] data, int offset, int len) throws OscException {
    for (int i = offset; i < len; ++i) {
      if (data[i] == 0) {
        return new OscString(new String(data, offset, i-offset));
      }
    }
    throw new OscMalformedDataException("OscString has no terminating null character", data, offset, len);
  }

  @Override
  public char getTypeTag() {
    return OscTypeTag.STRING;
  }

  @Override
  public String toString() {
    return this.value;
  }

  public void serialize(ByteBuffer buffer) {
    byte[] bytes = this.value.getBytes();
    buffer.put(bytes);
    for (int i = bytes.length; i < this.byteLength; ++i) {
      buffer.put((byte) 0);
    }
  }

  @Override
  public int toInt() {
    return Integer.parseInt(this.value);
  }

  @Override
  public float toFloat() {
    return Float.parseFloat(this.value);
  }

  @Override
  public double toDouble() {
    return Double.parseDouble(this.value);
  }
}

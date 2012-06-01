package com.continuuity.data.table.converter;

import java.util.Map;

import com.continuuity.data.operation.executor.omid.TimestampOracle;
import com.continuuity.data.table.ColumnarTable;
import com.continuuity.data.table.SimpleReadPointer;
import com.continuuity.data.table.VersionedColumnarTable;

public class ColumnarOnVersionedColumnarTable implements ColumnarTable {

  private final VersionedColumnarTable table;

  private final TimestampOracle oracle;

  public ColumnarOnVersionedColumnarTable(VersionedColumnarTable table,
      TimestampOracle timestampOracle) {
    this.table = table;
    this.oracle = timestampOracle;
  }

  @Override
  public void put(byte[] row, byte[] column, byte[] value) {
    this.table.put(row, column, this.oracle.getTimestamp(), value);
  }

  @Override
  public void put(byte[] row, byte[][] columns, byte[][] values) {
    this.table.put(row, columns, this.oracle.getTimestamp(), values);
  }

  @Override
  public void delete(byte[] row) {
    this.table.delete(row, this.oracle.getTimestamp());
  }

  @Override
  public void delete(byte[] row, byte[] column) {
    this.table.delete(row, column, this.oracle.getTimestamp());
  }

  @Override
  public Map<byte[], byte[]> get(byte[] row) {
    return this.table.get(row,
        new SimpleReadPointer(this.oracle.getTimestamp()));
  }

  @Override
  public byte[] get(byte[] row, byte[] column) {
    return this.table.get(row, column,
        new SimpleReadPointer(this.oracle.getTimestamp()));
  }

  @Override
  public Map<byte[], byte[]> get(byte[] row, byte[] startColumn,
      byte[] stopColumn) {
    return this.table.get(row, startColumn, stopColumn,
        new SimpleReadPointer(this.oracle.getTimestamp()));
  }

  @Override
  public Map<byte[], byte[]> get(byte[] row, byte[][] columns) {
    return this.table.get(row, columns,
        new SimpleReadPointer(this.oracle.getTimestamp()));
  }

  @Override
  public long increment(byte[] row, byte[] column, long amount) {
    long now = this.oracle.getTimestamp();
    return this.table.increment(row, column, amount,
        new SimpleReadPointer(now), now);
  }

  @Override
  public boolean compareAndSwap(byte[] row, byte[] column,
      byte[] expectedValue, byte[] newValue) {
    long now = this.oracle.getTimestamp();
    return this.table.compareAndSwap(row, column, expectedValue, newValue,
        new SimpleReadPointer(now), now);
  }

}

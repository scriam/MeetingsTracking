import React from 'react'
import { useState, useEffect } from "react";
import styled from 'styled-components'
import { useTable, useFilters, useGlobalFilter, useAsyncDebounce } from 'react-table'

// A great library for fuzzy filtering/sorting items
import matchSorter from 'match-sorter'

import Moment from 'moment'
import Form from './Form'
import getData from './getData'

const Styles = styled.div`
  padding: 1rem;

  table {
    border-spacing: 0;
    border: 1px solid black;

    tr {
      :last-child {
        td {
          border-bottom: 0;
        }
      }
    }

    th,
    td {
      margin: 0;
      padding: 0.5rem;
      border-bottom: 1px solid black;
      border-right: 1px solid black;

      :last-child {
        border-right: 0;
      }
    }
  }
`

// Define a default UI for filtering
function GlobalFilter({
  preGlobalFilteredRows,
  globalFilter,
  setGlobalFilter,
}) {
  const count = preGlobalFilteredRows.length
  const [value, setValue] = React.useState(globalFilter)
  const onChange = useAsyncDebounce(value => {
    setGlobalFilter(value || undefined)
  }, 200)

  return (
    <span>
      Search:{' '}
      <input
        value={value || ""}
        onChange={e => {
          setValue(e.target.value);
          onChange(e.target.value);
        }}
        placeholder={`${count} records...`}
        style={{
          fontSize: '1.1rem',
          border: '0',
        }}
      />
    </span>
  )
}

// Define a default UI for filtering
function DefaultColumnFilter({
  column: { filterValue, preFilteredRows, setFilter },
}) {
  const count = preFilteredRows.length

  return (
    <input
      value={filterValue || ''}
      onChange={e => {
        setFilter(e.target.value || undefined) // Set undefined to remove the filter entirely
      }}
      placeholder={`Search ${count} records...`}
    />
  )
}

function DateRangeColumnFilter({
  column: {
    filterValue = [],
    preFilteredRows,
    setFilter,
    id
  }})
{
  const [min, max] = React.useMemo(() => {
    //let min = preFilteredRows.length ? new Date(preFilteredRows[0].values[id]) : new Date(0)
   // let max = preFilteredRows.length ? new Date(preFilteredRows[0].values[id]) : new Date(0)
   let min = new Date("01-01-2100");
   let max = new Date(0);
    preFilteredRows.forEach(row => {
      //const rowDate = new Date(row.values[id])
      //const rowDate = Date.parse(row.values[id]);
      const rowDate = Moment(row.values[id],"DD-MM-YYYY").toDate();
     /* console.log("AA1 " + min);
      console.log("AA2 " + max);
      console.log("AA3 " + row.values[id]);
      console.log("AA4 " + rowDate); */
      min = rowDate <= min ? rowDate : min
      max = rowDate >= max ? rowDate : max
    })

    return [min, max]
  }, [id, preFilteredRows])

  return (
    <div>
      <input
        min={min.toISOString().slice(0, 10)}
        onChange={e => {
          const val = e.target.value
          setFilter((old = []) => [val ? val : undefined, old[1]])
        }}
        type="date"
        value={filterValue[0] || ''}
      />
      {' to '}
      <input
        max={max.toISOString().slice(0, 10)}
        onChange={e => {
          const val = e.target.value
          setFilter((old = []) => [old[0], val ? val.concat('T23:59:59.999Z') : undefined])
        }}
        type="date"
        value={filterValue[1]?.slice(0, 10) || ''}
      />
    </div>
  )
}

function dateBetweenFilterFn(rows, id, filterValues) {
  let sd = new Date(filterValues[0]);
  let ed = new Date(filterValues[1]);
  return rows.filter(r => {
      var time = new Date(r.values[id]);
      var time = Moment(r.values[id], "DD-MM-YYYY").toDate();
      if (filterValues.length === 0) return rows;
      if (isNaN(ed) && isNaN(sd)) return true;
      if (isNaN(ed)) return time>=sd;
      if (isNaN(sd)) return time<=ed;
      return (time >= sd && time <= ed);
  });
}

dateBetweenFilterFn.autoRemove = val => !val;

function fuzzyTextFilterFn(rows, id, filterValue) {
  return matchSorter(rows, filterValue, { keys: [row => row.values[id]] })
}

// Let the table remove the filter if the string is empty
fuzzyTextFilterFn.autoRemove = val => !val

// Our table component
function Table({ columns, data}) {
  const filterTypes = React.useMemo(
    () => ({
      // Add a new fuzzyTextFilterFn filter type.
      fuzzyText: fuzzyTextFilterFn,
      dateBetween: dateBetweenFilterFn,
      // Or, override the default text filter to use
      // "startWith"
      text: (rows, id, filterValue) => {
        return rows.filter(row => {
          const rowValue = row.values[id]
          return rowValue !== undefined
            ? String(rowValue)
                .toLowerCase()
                .startsWith(String(filterValue).toLowerCase())
            : true
        })
      },
    }),
    []
  )

  const defaultColumn = React.useMemo(
    () => ({
      Filter: DefaultColumnFilter,
    }),
    []
  )

  const {
    getTableProps,
    getTableBodyProps,
    headerGroups,
    rows,
    prepareRow,
    state,
    visibleColumns,
    preGlobalFilteredRows,
    setGlobalFilter,
  } = useTable(
    {
      columns,
      data,
      defaultColumn, // Be sure to pass the defaultColumn option
      filterTypes,
      // updateData,
    },
    useFilters, // useFilters!
    useGlobalFilter // useGlobalFilter!
  )

  // Render only 50 rows
  const firstPageRows = rows.slice(0, 50)

  return (
    <>
      <table {...getTableProps()}>
        <thead>
          {headerGroups.map(headerGroup => (
            <tr {...headerGroup.getHeaderGroupProps()}>
              {headerGroup.headers.map(column => (
                <th {...column.getHeaderProps()}>
                  {column.render('Header')}
                  {/* Render the columns filter UI */}
                  <div>{column.canFilter ? column.render('Filter') : null}</div>
                </th>
              ))}
            </tr>
          ))}
          <tr>
            <th
              colSpan={visibleColumns.length}
              style={{
                textAlign: 'left',
              }}
            >
              <GlobalFilter
                preGlobalFilteredRows={preGlobalFilteredRows}
                globalFilter={state.globalFilter}
                setGlobalFilter={setGlobalFilter}
              />
            </th>
          </tr>
        </thead>
        <tbody {...getTableBodyProps()}>
          {firstPageRows.map((row, i) => {
            prepareRow(row)
            return (
              <tr {...row.getRowProps()}>
                {row.cells.map(cell => {
                  return <td {...cell.getCellProps()}>{cell.render('Cell')}</td>
                })}
              </tr>
            )
          })}
        </tbody>
      </table>
      <br />
    </>
  )
}

function App() {
  const columns = React.useMemo(
    () => [
      {
        Header: 'Person',
        accessor: 'person',
        filter: 'fuzzyText',
      },
      {
        Header: 'Date',
        accessor: 
        row => {
        return Moment(row.date)
            .local()
            .format("DD-MM-YYYY") 
        },
        Filter: DateRangeColumnFilter,
        filter: 'dateBetween',
      },

      {
        Header: 'Location',
        accessor: 'location',
      },
    ],
    []
  )

  const [data, setData] = React.useState([]);

  useEffect(() => {
    getData().then(data => setData(data));
  },[]); 

  return (
    <Styles>
      <Form setData={setData}/>
      <Table columns={columns} data={data} />
    </Styles>
  )
}

export default App
